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

    override suspend fun likePreset(presetId: String, uid: String): Result<Unit> =
        firestoreDatasource.addLikedUserIdToPresetStorage(presetId, uid)

    override suspend fun unlikePreset(presetId: String, uid: String): Result<Unit> =
        firestoreDatasource.removeLikedUserIdFromPresetStorage(presetId, uid)

    override suspend fun savePresetForUser(uid: String, presetId: String): Result<Unit> {
        val userSideResult = firestoreDatasource.addPresetIdToUserStorage(uid, presetId)
        if (userSideResult.isFailure) return userSideResult

        val presetSideResult =
            firestoreDatasource.addUserIdToPresetSavedUsers(presetId, uid)
        if (presetSideResult.isFailure) {
            firestoreDatasource.removePresetIdFromUserStorage(uid, presetId)
        }
        return presetSideResult
    }

    override suspend fun unsavePresetForUser(uid: String, presetId: String): Result<Unit> {
        val userSideResult = firestoreDatasource.removePresetIdFromUserStorage(uid, presetId)
        if (userSideResult.isFailure) return userSideResult

        val presetSideResult =
            firestoreDatasource.removeUserIdFromPresetSavedUsers(presetId, uid)
        if (presetSideResult.isFailure) {
            firestoreDatasource.addPresetIdToUserStorage(uid, presetId)
        }
        return presetSideResult
    }

    override suspend fun deletePost(uid: String, presetId: String): Result<Unit> =
        firestoreDatasource.deleteUserPost(uid, presetId)
}
