package com.orukunnn.shapesnapapp.data.model.trend

import kotlinx.serialization.Serializable

@Serializable
data class TrendItem(
    val presetId: String,
    val score: Double,
    val rank: Int,
    val likeCount: Int,
    val saveCount: Int,
    val impressionCount: Int,
)
