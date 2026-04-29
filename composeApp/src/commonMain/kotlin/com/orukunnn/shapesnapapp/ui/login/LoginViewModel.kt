package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.ui.common.LoadState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val credentialProvider: CredentialProvider,
) : ViewModel() {
    var state by mutableStateOf<LoadState<Unit>>(LoadState.Idle)
        private set

    fun signInWithGoogle() {
        viewModelScope.launch {
            state = LoadState.Loading
            val tokensResult = credentialProvider.getGoogleTokens()
            if (tokensResult.isFailure) {
                val e = tokensResult.exceptionOrNull() ?: Throwable("token error")
                state = LoadState.Error(e.message ?: "error")
                return@launch
            }
            val tokens = tokensResult.getOrThrow()
            val signIn =
                authRepository.signInWithGoogleCredentials(
                    idToken = tokens.idToken,
                    accessToken = tokens.accessToken,
                )
            state =
                if (signIn.isSuccess) {
                    LoadState.Success(Unit)
                } else {
                    LoadState.Error(signIn.exceptionOrNull()?.message ?: "error")
                }
        }
    }
}
