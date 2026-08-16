package com.orukunnn.shapesnapapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.trend.TrendPreset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetPageCursor
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.trend.TrendsRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.domain.EventLogger
import com.orukunnn.shapesnapapp.ui.common.StorageLimits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val presets: ImmutableList<Preset>,
        val trendPresets: ImmutableList<TrendPreset>,
        val hasMore: Boolean,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

class HomeScreenViewModel(
    private val userProfile: UserProfile,
    private val presetRepository: PresetRepository,
    private val trendsRepository: TrendsRepository,
    private val userRepository: UserRepository,
    private val eventLogger: EventLogger,
) : ViewModel() {
    private val pagingState = MutableStateFlow(HomePresetPagingState())
    private var pagingGeneration: Int = 0

    val homeState: StateFlow<HomeUiState> =
        combine(
            pagingState,
            trendsRepository.weeklyTrendPresets,
        ) { paging, trendPresets ->
            paging.toHomeUiState(trendPresets.orEmpty())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HomeUiState.Loading,
        )

    val isLoadingMore: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val loadMoreFailed: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val showLimitReachedDialog: StateFlow<Boolean>
        field = MutableStateFlow(false)

    private val storageIds: StateFlow<List<String>> =
        userRepository.observeUser(userProfile.uid)
            .map { it?.storage ?: userProfile.storage }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = userProfile.storage,
            )

    init {
        viewModelScope.launch {
            loadInitialPage(showRefreshIndicator = false)
        }
    }

    fun refreshPresets() {
        if (isRefreshing.value) return
        viewModelScope.launch {
            loadInitialPage(showRefreshIndicator = pagingState.value.initialized)
        }
    }

    fun toggleLike(presetId: String) {
        viewModelScope.launch {
            val isAlreadyLiked =
                findLoadedPreset(presetId)
                    ?.likedUserIds
                    ?.contains(userProfile.uid)
                    ?: false
            val updateLike =
                if (isAlreadyLiked) {
                    userRepository.unlikePreset(presetId, userProfile.uid)
                } else {
                    userRepository.likePreset(presetId, userProfile.uid)
                }
            updateLike
                .onSuccess {
                    updateLoadedPreset(presetId) { preset ->
                        val ids = preset.likedUserIds
                        preset.copy(
                            likedUserIds =
                                if (isAlreadyLiked) {
                                    ids - userProfile.uid
                                } else {
                                    ids + userProfile.uid
                                },
                        )
                    }
                    eventLogger.logPresetLike(
                        userId = userProfile.uid,
                        presetId = presetId,
                        isLike = !isAlreadyLiked,
                    )
                }
        }
    }

    fun toggleSave(presetId: String) {
        viewModelScope.launch {
            val isAlreadySaved =
                findLoadedPreset(presetId)
                    ?.savedUserIds
                    ?.contains(userProfile.uid)
                    ?: (presetId in storageIds.value)
            if (isAlreadySaved) {
                userRepository.unsavePresetForUser(userProfile.uid, presetId)
                    .onSuccess {
                        updateLoadedPreset(presetId) { preset ->
                            preset.copy(savedUserIds = preset.savedUserIds - userProfile.uid)
                        }
                    }
                return@launch
            }
            if (storageIds.value.size >= StorageLimits.FREE_LIMIT) {
                showLimitReachedDialog.value = true
                return@launch
            }
            userRepository
                .savePresetForUser(userProfile.uid, presetId)
                .onSuccess {
                    updateLoadedPreset(presetId) { preset ->
                        preset.copy(savedUserIds = preset.savedUserIds + userProfile.uid)
                    }
                    eventLogger.logPresetSave(
                        userId = userProfile.uid,
                        presetId = presetId,
                    )
                }
        }
    }

    fun logTrendItemClick(presetId: String) {
        eventLogger.logTrendItemClick(userProfile.uid, presetId)
    }

    fun dismissLimitDialog() {
        showLimitReachedDialog.value = false
    }

    private suspend fun loadInitialPage(showRefreshIndicator: Boolean) {
        val generation = ++pagingGeneration
        if (showRefreshIndicator) {
            isRefreshing.value = true
        } else {
            pagingState.value = HomePresetPagingState()
        }
        isLoadingMore.value = false
        loadMoreFailed.value = false

        val result = presetRepository.loadPresetsPage(null)
        if (generation != pagingGeneration) {
            if (showRefreshIndicator) isRefreshing.value = false
            return
        }
        result.fold(
            onSuccess = { (page, nextCursor) ->
                pagingState.value =
                    HomePresetPagingState(
                        items = page,
                        nextCursor = nextCursor,
                        hasMore = nextCursor != null,
                        initialized = true,
                    )
            },
            onFailure = {
                if (!pagingState.value.initialized) {
                    pagingState.value =
                        HomePresetPagingState(
                            errorMessage = INITIAL_LOAD_ERROR_MESSAGE,
                        )
                }
            },
        )
        isRefreshing.value = false
    }

    fun loadMore() {
        val current = pagingState.value
        if (!current.initialized || !current.hasMore) return
        if (isLoadingMore.value || isRefreshing.value) return

        isLoadingMore.value = true
        loadMoreFailed.value = false
        val generation = pagingGeneration
        val cursor = current.nextCursor
        viewModelScope.launch {
            val result = presetRepository.loadPresetsPage(cursor)
            if (generation != pagingGeneration) {
                isLoadingMore.value = false
                return@launch
            }
            result.fold(
                onSuccess = { (page, nextCursor) ->
                    pagingState.update { state ->
                        val existingIds = state.items.map { it.id }.toSet()
                        state.copy(
                            items = state.items + page.filter { it.id !in existingIds },
                            nextCursor = nextCursor,
                            hasMore = nextCursor != null,
                            errorMessage = null,
                        )
                    }
                    loadMoreFailed.value = false
                },
                onFailure = {
                    loadMoreFailed.value = true
                },
            )
            isLoadingMore.value = false
        }
    }

    private fun findLoadedPreset(presetId: String): Preset? =
        pagingState.value.items.firstOrNull { it.id == presetId }
            ?: presetRepository.presets.value?.firstOrNull { it.id == presetId }

    private fun updateLoadedPreset(presetId: String, transform: (Preset) -> Preset) {
        pagingState.update { state ->
            state.copy(
                items = state.items.map { preset ->
                    if (preset.id == presetId) transform(preset) else preset
                },
            )
        }
    }

    private companion object {
        const val INITIAL_LOAD_ERROR_MESSAGE: String = "プリセットを取得できませんでした"

        private fun HomePresetPagingState.toHomeUiState(
            trendPresets: List<TrendPreset>,
        ): HomeUiState =
            when {
                errorMessage != null && !initialized ->
                    HomeUiState.Error(errorMessage)
                !initialized ->
                    HomeUiState.Loading
                else ->
                    HomeUiState.Success(
                        presets = items.toPersistentList(),
                        trendPresets = trendPresets.toPersistentList(),
                        hasMore = hasMore,
                    )
            }
    }
}

private data class HomePresetPagingState(
    val items: List<Preset> = emptyList(),
    val nextCursor: PresetPageCursor? = null,
    val hasMore: Boolean = true,
    val initialized: Boolean = false,
    val errorMessage: String? = null,
)
