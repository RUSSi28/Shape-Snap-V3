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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostsScreenViewModel(
    private val userId: String,
    presetRepository: PresetRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _postPendingDeletion = MutableStateFlow<Preset?>(null)
    val postPendingDeletion: StateFlow<Preset?> = _postPendingDeletion.asStateFlow()

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting.asStateFlow()

    private val _deleteFailed = MutableStateFlow(false)
    val deleteFailed: StateFlow<Boolean> = _deleteFailed.asStateFlow()

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
        _deleteFailed.value = false
        _postPendingDeletion.value = preset
    }

    fun dismissPostDeletion() {
        if (_isDeleting.value) return
        _deleteFailed.value = false
        _postPendingDeletion.value = null
    }

    fun confirmPostDeletion() {
        val preset = _postPendingDeletion.value ?: return
        if (_isDeleting.value) return

        viewModelScope.launch {
            _isDeleting.value = true
            _deleteFailed.value = false
            val result = userRepository.deletePost(userId, preset.id)
            _isDeleting.value = false
            if (result.isSuccess) {
                _postPendingDeletion.value = null
            } else {
                _deleteFailed.value = true
            }
        }
    }
}
