package com.orukunnn.shapesnapapp.ui.storage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StoredPreset(
    val id: String,
    val imageUrl: String?,
)

sealed interface StorageUiState {
    data object Loading : StorageUiState

    data class Success(
        val presets: ImmutableList<StoredPreset>,
        val presetPendingRemoval: StoredPreset? = null,
        val isRemoving: Boolean = false,
        val removeFailed: Boolean = false,
    ) : StorageUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
class StorageScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val presetRepository: PresetRepository,
) : ViewModel() {
    private val removalState = MutableStateFlow(RemovalState())

    val state: StateFlow<StorageUiState> =
        combine(
            authRepository.currentUser.flatMapLatest { auth ->
                if (auth == null) {
                    flowOf(null)
                } else {
                    userRepository.observeUser(auth.uid)
                }
            },
            presetRepository.presets,
            removalState,
        ) { userProfile, presets, removal ->
            if (userProfile == null || presets == null) {
                StorageUiState.Loading
            } else {
                val presetMap = presets.associateBy { it.id }
                StorageUiState.Success(
                    presets = userProfile.storage.map { id ->
                        val preset = presetMap[id]
                        StoredPreset(
                            id = id,
                            imageUrl = preset?.imageUrl ?: preset?.previewImageUrl,
                        )
                    }.toPersistentList(),
                    presetPendingRemoval = removal.pending,
                    isRemoving = removal.isRemoving,
                    removeFailed = removal.removeFailed,
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = StorageUiState.Loading,
        )

    fun requestRemoval(preset: StoredPreset) {
        removalState.value = RemovalState(pending = preset)
    }

    fun dismissRemoval() {
        if (removalState.value.isRemoving) return
        removalState.value = RemovalState()
    }

    fun confirmRemoval() {
        val preset = removalState.value.pending ?: return
        if (removalState.value.isRemoving) return

        viewModelScope.launch {
            removalState.update { it.copy(isRemoving = true, removeFailed = false) }
            val uid = authRepository.currentUser.filterNotNull().first().uid
            val result = userRepository.unsavePresetForUser(uid, preset.id)
            if (result.isSuccess) {
                removalState.value = RemovalState()
            } else {
                removalState.update { it.copy(isRemoving = false, removeFailed = true) }
            }
        }
    }

    private data class RemovalState(
        val pending: StoredPreset? = null,
        val isRemoving: Boolean = false,
        val removeFailed: Boolean = false,
    )
}
