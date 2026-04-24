package com.orukunnn.shapesnapapp.data.model.preset


import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class PresetEntity(
    val displayName: String = "",
    val name: String = "",
    val description: String? = null,
    val imageUrl: String? = null,
    val previewImageUrl: String? = null,
    val characterTagId: String = "",
    val likedUserIds: List<String> = emptyList(),
    val savedUserIds: List<String> = emptyList(),
    val blendShapeWeights: Map<String, Double> = emptyMap(),
    val createdAtEpochSeconds: Long? = null,
)

fun PresetEntity.toPreset(documentId: String): Preset {
    val dn = displayName.ifBlank { name }
    val img = imageUrl ?: previewImageUrl
    val created =
        Instant.fromEpochSeconds(
            createdAtEpochSeconds ?: 0,
            0,
        )
    return Preset(
        id = documentId,
        displayName = dn,
        description = description,
        imageUrl = img,
        createdAt = created,
        characterTagId = characterTagId,
        likedUserIds = likedUserIds,
        savedUserIds = savedUserIds,
        blendShapeWeights = blendShapeWeights,
    )
}
