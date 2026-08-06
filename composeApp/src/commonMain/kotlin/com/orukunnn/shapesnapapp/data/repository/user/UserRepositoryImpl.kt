package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource

class UserRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource,
) : UserRepository {
    override fun observeUser(uid: String) = firestoreDatasource.observeUser(uid)

    override suspend fun ensureUserDocument(
        uid: String,
        displayName: String?,
        photoUrl: String?,
    ): Result<Unit> = firestoreDatasource.ensureUserDocument(uid, displayName, photoUrl)

    override suspend fun togglePresetLike(presetId: String, uid: String): Result<Unit> =
        firestoreDatasource.togglePresetLike(presetId, uid)

    override suspend fun addPresetToStorage(uid: String, presetId: String): Result<Unit> =
        firestoreDatasource.addPresetToUserStorage(uid, presetId)

    override suspend fun removePresetFromStorage(uid: String, presetId: String): Result<Unit> =
        firestoreDatasource.removePresetFromUserStorage(uid, presetId)

    override suspend fun deletePost(uid: String, presetId: String): Result<Unit> =
        firestoreDatasource.deleteUserPost(uid, presetId)
}
