package com.orukunnn.shapesnapapp.data.repository.user

import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.datasource.UserPostsPage
import com.orukunnn.shapesnapapp.data.model.user.UserPost

interface UserPostsRepository {
    suspend fun fetchPage(
        uid: String,
        pageSize: Int,
        startAfterDocumentId: String?,
    ): Result<UserPostsPage>
}

class UserPostsRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource,
) : UserPostsRepository {
    override suspend fun fetchPage(
        uid: String,
        pageSize: Int,
        startAfterDocumentId: String?,
    ): Result<UserPostsPage> = firestoreDatasource.fetchUserPosts(uid, pageSize, startAfterDocumentId)
}
