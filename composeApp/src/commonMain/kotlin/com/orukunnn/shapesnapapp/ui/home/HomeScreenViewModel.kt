package com.orukunnn.shapesnapapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.domain.EventLogger
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val presets: ImmutableList<Preset>,
        val hasMore: Boolean,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModel(
    private val userProfile: UserProfile,
    private val presetRepository: PresetRepository,
    private val userRepository: UserRepository,
    private val eventLogger: EventLogger,
) : ViewModel() {
    val homeState: StateFlow<HomeUiState> =
        presetRepository.presets
            .map { presets -> presets.toHomeUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = presetRepository.presets.value.toHomeUiState(),
            )


    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _showLimitReachedDialog = MutableStateFlow(false)
    val showLimitReachedDialog: StateFlow<Boolean> = _showLimitReachedDialog.asStateFlow()

    private val _signInState = MutableStateFlow<LoadState<Unit>>(LoadState.Idle)
    val signInState: StateFlow<LoadState<Unit>> = _signInState.asStateFlow()

    fun loadMore() {
        // スナップショットで一覧をまとめて受け取るため追加ページはなし
    }

    fun refreshPresets() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(300)
            _isRefreshing.value = false
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
            userRepository
                .togglePresetLike(presetId, userProfile.uid)
                .onSuccess {
                    eventLogger.logPresetLike(
                        userId = userProfile.uid,
                        presetId = presetId,
                        isLike = !isAlreadyLiked,
                    )
                }
        }
    }

    fun saveToStorage(presetId: String) {
        viewModelScope.launch {
            if (presetId in userProfile.storage) return@launch
            if (userProfile.storage.size >= FREE_LIMIT) {
                _showLimitReachedDialog.value = true
                return@launch
            }
            userRepository
                .addPresetToStorage(userProfile.uid, presetId)
                .onSuccess {
                    eventLogger.logPresetSave(
                        userId = userProfile.uid,
                        presetId = presetId,
                    )
                }
        }
    }

    fun logPresetImpression(presetId: String) {
        eventLogger.logPresetImpression(userProfile.uid, presetId)
    }

    fun dismissLimitDialog() {
        _showLimitReachedDialog.value = false
    }

    companion object {
        const val FREE_LIMIT: Int = 5

        private fun List<Preset>?.toHomeUiState(): HomeUiState =
            when (this) {
                null ->
                    HomeUiState.Loading
                else ->
                    HomeUiState.Success(
                        presets = sortedByDescending { it.id }.toPersistentList(),
                        hasMore = false,
                    )
            }
    }
}
