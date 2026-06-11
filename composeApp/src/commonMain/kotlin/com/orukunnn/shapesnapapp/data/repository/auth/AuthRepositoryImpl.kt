package com.orukunnn.shapesnapapp.data.repository.auth

import com.orukunnn.shapesnapapp.data.datasource.AuthDatasource
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class AuthRepositoryImpl(
    private val authDatasource: AuthDatasource,
    scope: CoroutineScope,
) : AuthRepository {
    private val _isAuthReady =
        MutableStateFlow(authDatasource.currentUserSnapshot() != null)

    override val isAuthReady: StateFlow<Boolean> = _isAuthReady.asStateFlow()

    override val currentUser: StateFlow<UserProfile?> =
        authDatasource.authState
            .onEach { _isAuthReady.value = true }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = authDatasource.currentUserSnapshot(),
            )

    override suspend fun signInWithGoogleCredentials(
        idToken: String,
        accessToken: String?,
    ): Result<Unit> = authDatasource.signInWithGoogleCredentials(idToken, accessToken)

    override suspend fun signOut() {
        authDatasource.signOut()
    }
}
