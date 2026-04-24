package com.orukunnn.shapesnapapp.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val displayName: String? = null,
    val photoUrl: String? = null,
    val storage: List<String> = emptyList(),
)

fun UserEntity.toProfile(uid: String) =
    UserProfile(
        uid = uid,
        displayName = displayName,
        photoUrl = photoUrl,
        storage = storage,
    )
