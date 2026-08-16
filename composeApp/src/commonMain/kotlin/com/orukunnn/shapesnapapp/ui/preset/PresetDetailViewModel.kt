package com.orukunnn.shapesnapapp.ui.preset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.domain.EventLogger
import com.orukunnn.shapesnapapp.ui.common.StorageLimits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface PresetDetailUiState {
    data object Loading : PresetDetailUiState

    data class Success(
        val preset: Preset,
    ) : PresetDetailUiState

    data object NotFound : PresetDetailUiState

    data class Error(
        val message: String,
    ) : PresetDetailUiState
}

class PresetDetailViewModel(
    private val presetId: String,
    private val userProfile: UserProfile,
    private val presetRepository: PresetRepository,
    private val userRepository: UserRepository,
    private val eventLogger: EventLogger,
) : ViewModel() {
    val state: StateFlow<PresetDetailUiState> =
        presetRepository.presets
            .map { presets -> presets.toDetailUiState(presetId) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = presetRepository.presets.value.toDetailUiState(presetId),
            )

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

    fun toggleLike() {
        val preset = (state.value as? PresetDetailUiState.Success)?.preset ?: return
        viewModelScope.launch {
            val isAlreadyLiked = userProfile.uid in preset.likedUserIds
            val result =
                if (isAlreadyLiked) {
                    userRepository.unlikePreset(preset.id, userProfile.uid)
                } else {
                    userRepository.likePreset(preset.id, userProfile.uid)
                }
            result.onSuccess {
                eventLogger.logPresetLike(
                    userId = userProfile.uid,
                    presetId = preset.id,
                    isLike = !isAlreadyLiked,
                )
            }
        }
    }

    fun toggleSave() {
        val preset = (state.value as? PresetDetailUiState.Success)?.preset ?: return
        viewModelScope.launch {
            val isAlreadySaved = userProfile.uid in preset.savedUserIds
            if (isAlreadySaved) {
                userRepository.unsavePresetForUser(userProfile.uid, preset.id)
                return@launch
            }
            if (storageIds.value.size >= StorageLimits.FREE_LIMIT) {
                showLimitReachedDialog.value = true
                return@launch
            }
            userRepository.savePresetForUser(userProfile.uid, preset.id).onSuccess {
                eventLogger.logPresetSave(
                    userId = userProfile.uid,
                    presetId = preset.id,
                )
            }
        }
    }

    fun dismissLimitDialog() {
        showLimitReachedDialog.value = false
    }

    companion object {
        private fun List<Preset>?.toDetailUiState(presetId: String): PresetDetailUiState =
            when (this) {
                null -> PresetDetailUiState.Loading
                else ->
                    firstOrNull { it.id == presetId }
                        ?.let(PresetDetailUiState::Success)
                        ?: PresetDetailUiState.NotFound
            }
    }
}
