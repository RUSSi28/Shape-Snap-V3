package com.orukunnn.shapesnapapp.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserPost(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val createdAt: Long = 0L,
)
