package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthDatasourceImpl : AuthDatasource {

    private val auth get() = Firebase.auth

    override val authState: Flow<UserProfile?> =
        auth.authStateChanged.map { user -> user?.toUserProfile() }

    override fun currentUserSnapshot(): UserProfile? =
        auth.currentUser?.toUserProfile()

    override suspend fun signInWithGoogleCredentials(
        idToken: String,
        accessToken: String?,
    ): Result<Unit> =
        runCatching {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)
            auth.signInWithCredential(credential)
        }

    override suspend fun signOut() {
        auth.signOut()
    }
}

private fun FirebaseUser.toUserProfile(): UserProfile =
    UserProfile(
        uid = uid,
        displayName = displayName,
        email = email,
        photoUrl = photoURL,
    )
