package com.orukunnn.shapesnapapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val credentialProvider: CredentialProvider,
) : ViewModel() {
    private val _state = MutableStateFlow<LoadState<Unit>>(LoadState.Idle)
    val state: StateFlow<LoadState<Unit>> = _state.asStateFlow()

    fun signInWithGoogle() {
        viewModelScope.launch {
            _state.value = LoadState.Loading
            val tokensResult = credentialProvider.getGoogleTokens()
            if (tokensResult.isFailure) {
                val e = tokensResult.exceptionOrNull() ?: Throwable("token error")
                _state.value = LoadState.Error(e.message ?: "error")
                return@launch
            }
            val tokens = tokensResult.getOrThrow()
            val signIn =
                authRepository.signInWithGoogleCredentials(
                    idToken = tokens.idToken,
                    accessToken = tokens.accessToken,
                )
            _state.value =
                if (signIn.isSuccess) {
                    LoadState.Success(Unit)
                } else {
                    LoadState.Error(signIn.exceptionOrNull()?.message ?: "error")
                }
        }
    }
}
