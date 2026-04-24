package com.orukunnn.shapesnapapp.data.model.preset

import kotlinx.datetime.Instant

data class Preset(
    val id: String = "",
    val displayName: String = "",
    val description: String? = null,
    val imageUrl: String? = null,
    val createdAt: Instant = Instant.fromEpochSeconds(0, 0),
    val characterTagId: String = "",
    val likedUserIds: List<String> = emptyList(),
    val savedUserIds: List<String> = emptyList(),
    val blendShapeWeights: Map<String, Double> = emptyMap(),
) {
    val name: String get() = displayName

    val previewImageUrl: String? get() = imageUrl
}
