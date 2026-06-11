package com.orukunnn.shapesnapapp.data.repository.auth

import com.orukunnn.shapesnapapp.data.datasource.AuthDatasource
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authDatasource: AuthDatasource,
) : AuthRepository {

    override val currentUser: Flow<UserProfile?> =
        authDatasource.authState

    override suspend fun signInWithGoogleCredentials(
        idToken: String,
        accessToken: String?,
    ): Result<Unit> = authDatasource.signInWithGoogleCredentials(idToken, accessToken)

    override suspend fun signOut() {
        authDatasource.signOut()
    }
}
