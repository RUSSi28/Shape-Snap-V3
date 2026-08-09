package com.orukunnn.shapesnapapp.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostsScreenViewModel(
    private val userId: String,
    presetRepository: PresetRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val postPendingDeletion: StateFlow<Preset?>
        field = MutableStateFlow<Preset?>(null)

    val isDeleting: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val deleteFailed: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val state: StateFlow<LoadState<List<Preset>>> =
        combine(
            userRepository.observeUser(userId),
            presetRepository.presets,
        ) { userProfile, presets ->
            if (userProfile == null || presets == null) {
                LoadState.Loading
            } else {
                val presetsById = presets.associateBy(Preset::id)
                LoadState.Success(
                    userProfile.posts.mapNotNull(presetsById::get),
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LoadState.Loading,
        )

    fun requestPostDeletion(preset: Preset) {
        deleteFailed.value = false
        postPendingDeletion.value = preset
    }

    fun dismissPostDeletion() {
        if (isDeleting.value) return
        deleteFailed.value = false
        postPendingDeletion.value = null
    }

    fun confirmPostDeletion() {
        val preset = postPendingDeletion.value ?: return
        if (isDeleting.value) return

        viewModelScope.launch {
            isDeleting.value = true
            deleteFailed.value = false
            val result = userRepository.deletePost(userId, preset.id)
            isDeleting.value = false
            if (result.isSuccess) {
                postPendingDeletion.value = null
            } else {
                deleteFailed.value = true
            }
        }
    }
}
