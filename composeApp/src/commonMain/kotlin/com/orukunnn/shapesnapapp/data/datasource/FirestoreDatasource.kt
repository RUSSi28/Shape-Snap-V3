package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.user.UserPost
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.data.repository.preset.PresetPageCursor
import kotlinx.coroutines.flow.Flow

data class UserPostsPage(
    val items: List<UserPost>,
    val nextCursor: String?,
)

interface FirestoreDatasource {
    fun observePresets(): Flow<List<Preset>>

    suspend fun fetchPresetsPage(
        pageSize: Int,
        cursor: PresetPageCursor?,
    ): Result<Pair<List<Preset>, PresetPageCursor?>>

    fun observeUser(uid: String): Flow<UserProfile?>

    suspend fun ensureUserDocument(
        uid: String,
        displayName: String?,
        photoUrl: String?,
    ): Result<Unit>

    suspend fun addLikedUserIdToPresetStorage(presetId: String, uid: String): Result<Unit>

    suspend fun removeLikedUserIdFromPresetStorage(presetId: String, uid: String): Result<Unit>

    suspend fun addPresetIdToUserStorage(uid: String, presetId: String): Result<Unit>

    suspend fun removePresetIdFromUserStorage(uid: String, presetId: String): Result<Unit>

    suspend fun addUserIdToPresetSavedUsers(presetId: String, uid: String): Result<Unit>

    suspend fun removeUserIdFromPresetSavedUsers(presetId: String, uid: String): Result<Unit>

    suspend fun deleteUserPost(uid: String, presetId: String): Result<Unit>

    suspend fun fetchUserPosts(
        uid: String,
        pageSize: Int,
        startAfterDocumentId: String?,
    ): Result<UserPostsPage>
}
