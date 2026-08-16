package com.orukunnn.shapesnapapp.data.model.trend

import kotlinx.serialization.Serializable

@Serializable
data class TrendRanking(
    val period: String = "",
    val windowStartEpochSeconds: Long = 0,
    val windowEndEpochSeconds: Long = 0,
    val items: List<TrendItem> = emptyList(),
    val updatedAtEpochSeconds: Long = 0,
)
