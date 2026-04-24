package com.orukunnn.shapesnapapp.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.data.datasource.KeyValueDatasource
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.model.user.mergeWithFirestore
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val keyValueDatasource: KeyValueDatasource,
) : ViewModel() {
    val sheetUserProfile: StateFlow<UserProfile?> =
        authRepository.currentUser
            .flatMapLatest { auth ->
                if (auth == null) {
                    flowOf(null)
                } else {
                    userRepository.observeUser(auth.uid).map { firestore ->
                        auth.mergeWithFirestore(firestore)
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    private val _showLogoutConfirmDialog = MutableStateFlow(false)
    val showLogoutConfirmDialog: StateFlow<Boolean> = _showLogoutConfirmDialog.asStateFlow()

    fun requestLogoutConfirmation() {
        _showLogoutConfirmDialog.value = true
    }

    fun dismissLogoutConfirmation() {
        _showLogoutConfirmDialog.value = false
    }

    fun confirmLogout() {
        viewModelScope.launch {
            authRepository.signOut()
            keyValueDatasource.clear()
            _showLogoutConfirmDialog.value = false
        }
    }
}
