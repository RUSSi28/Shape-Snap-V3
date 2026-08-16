package com.orukunnn.shapesnapapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.trend.TrendPreset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.trend.TrendsRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.domain.EventLogger
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    val homeState: StateFlow<HomeUiState> =
        combine(
            presetRepository.presets,
            trendsRepository.weeklyTrendPresets,
        ) { presets, trendPresets ->
            presets.toHomeUiState(trendPresets.orEmpty())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue =
                presetRepository.presets.value.toHomeUiState(
                    trendsRepository.weeklyTrendPresets.value.orEmpty(),
                ),
        )


    val isLoadingMore: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val showLimitReachedDialog: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val signInState: StateFlow<LoadState<Unit>>
        field = MutableStateFlow<LoadState<Unit>>(LoadState.Idle)

    fun loadMore() {
        // スナップショットで一覧をまとめて受け取るため追加ページはなし
    }

    fun refreshPresets() {
        viewModelScope.launch {
            isRefreshing.value = true
            delay(300)
            isRefreshing.value = false
        }
    }

    fun toggleLike(presetId: String) {
        viewModelScope.launch {
            val isAlreadyLiked =
                presetRepository.presets.value
                    ?.firstOrNull { it.id == presetId }
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
            if (presetId in userProfile.storage) {
                userRepository.unsavePresetForUser(userProfile.uid, presetId)
                return@launch
            }
            if (userProfile.storage.size >= FREE_LIMIT) {
                showLimitReachedDialog.value = true
                return@launch
            }
            userRepository
                .savePresetForUser(userProfile.uid, presetId)
                .onSuccess {
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

    companion object {
        const val FREE_LIMIT: Int = 5

        private fun List<Preset>?.toHomeUiState(
            trendPresets: List<TrendPreset>,
        ): HomeUiState =
            when (this) {
                null ->
                    HomeUiState.Loading
                else ->
                    HomeUiState.Success(
                        presets = sortedByDescending { it.createdAt }.toPersistentList(),
                        trendPresets = trendPresets.toPersistentList(),
                        hasMore = false,
                    )
            }
    }
}
