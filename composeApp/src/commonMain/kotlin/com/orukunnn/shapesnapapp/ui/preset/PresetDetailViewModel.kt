package com.orukunnn.shapesnapapp.ui.preset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.domain.EventLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val state: StateFlow<PresetDetailUiState>
        field = MutableStateFlow<PresetDetailUiState>(PresetDetailUiState.Loading)

    init {
        loadPreset()
    }

    fun loadPreset() {
        viewModelScope.launch {
            state.value = PresetDetailUiState.Loading
            presetRepository.fetchPresetById(presetId).fold(
                onSuccess = { preset ->
                    state.value = preset?.let(PresetDetailUiState::Success)
                        ?: PresetDetailUiState.NotFound
                },
                onFailure = {
                    state.value = PresetDetailUiState.Error(
                        message = "プリセットを読み込めませんでした。もう一度お試しください。",
                    )
                },
            )
        }
    }

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
            if (preset.id in userProfile.storage) {
                userRepository.unsavePresetForUser(userProfile.uid, preset.id)
            } else {
                userRepository.savePresetForUser(userProfile.uid, preset.id).onSuccess {
                    eventLogger.logPresetSave(
                        userId = userProfile.uid,
                        presetId = preset.id,
                    )
                }
            }
        }
    }
}
