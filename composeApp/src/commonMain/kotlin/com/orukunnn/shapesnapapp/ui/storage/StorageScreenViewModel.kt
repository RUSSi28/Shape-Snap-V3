package com.orukunnn.shapesnapapp.ui.storage

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StorageScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val savedPresetIds: StateFlow<List<String>> =
        authRepository.currentUser
            .flatMapLatest { auth ->
                if (auth == null) {
                    flowOf(emptyList())
                } else {
                    userRepository.observeUser(auth.uid).map { it?.storage ?: emptyList() }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    fun removePreset(presetId: String) {
        viewModelScope.launch {
            val uid = authRepository.currentUser.filterNotNull().first().uid
            userRepository.removePresetFromStorage(uid, presetId)
        }
    }
}
