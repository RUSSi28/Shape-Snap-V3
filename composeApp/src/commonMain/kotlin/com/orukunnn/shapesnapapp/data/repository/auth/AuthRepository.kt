package com.orukunnn.shapesnapapp.data.repository.auth

import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<UserProfile?>

    fun currentUserSnapshot(): UserProfile?

    suspend fun signInWithGoogleCredentials(idToken: String, accessToken: String?): Result<Unit>

    suspend fun signOut()
}
