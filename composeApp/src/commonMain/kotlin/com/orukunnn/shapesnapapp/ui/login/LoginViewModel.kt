package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orukunnn.shapesnapapp.core.platform.CredentialProvider
import com.orukunnn.shapesnapapp.core.util.AppLogger
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
                state = LoadState.Error(userFacingTokenErrorMessage(e))
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

    /**
     * 設定不備などの [IllegalStateException] は開発者向け詳細をログに残し、
     * UI には管理者問い合わせの文言だけを返す。
     */
    private fun userFacingTokenErrorMessage(e: Throwable): String {
        if (e is IllegalStateException) {
            AppLogger.e(e.message ?: "IllegalStateException", e)
            return GOOGLE_LOGIN_UNAVAILABLE_MESSAGE
        }
        return e.message ?: "error"
    }

    private companion object {
        const val GOOGLE_LOGIN_UNAVAILABLE_MESSAGE =
            "Googleでログインが使えないため、管理者に問い合わせてください。"
    }
}
