package com.orukunnn.shapesnapapp.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val uid: String,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val storage: List<String> = emptyList(),
)

fun UserProfile.mergeWithFirestore(firestore: UserProfile?): UserProfile {
    if (firestore == null) return this
    return copy(
        displayName = firestore.displayName ?: displayName,
        photoUrl = firestore.photoUrl ?: photoUrl,
        storage = firestore.storage,
    )
}
