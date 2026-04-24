package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthDatasource {
    val authState: Flow<UserProfile?>

    suspend fun signInWithGoogleCredentials(idToken: String, accessToken: String?): Result<Unit>

    suspend fun signOut()
}
