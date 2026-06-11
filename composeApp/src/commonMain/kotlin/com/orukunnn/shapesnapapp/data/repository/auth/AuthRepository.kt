package com.orukunnn.shapesnapapp.data.repository.auth

import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<UserProfile?>

    /** Firebase の初回 auth 通知を受け取ったら true。スナップショットでユーザーが分かる場合は最初から true。 */
    val isAuthReady: StateFlow<Boolean>

    suspend fun signInWithGoogleCredentials(idToken: String, accessToken: String?): Result<Unit>

    suspend fun signOut()
}
