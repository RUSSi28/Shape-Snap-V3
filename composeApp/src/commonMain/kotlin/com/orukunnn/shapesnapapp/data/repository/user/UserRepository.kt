package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUser(uid: String): Flow<UserProfile?>

    suspend fun ensureUserDocument(
        uid: String,
        displayName: String?,
        photoUrl: String?,
    ): Result<Unit>

    suspend fun togglePresetLike(presetId: String, uid: String): Result<Unit>

    suspend fun addPresetToStorage(uid: String, presetId: String): Result<Unit>

    suspend fun removePresetFromStorage(uid: String, presetId: String): Result<Unit>
}
