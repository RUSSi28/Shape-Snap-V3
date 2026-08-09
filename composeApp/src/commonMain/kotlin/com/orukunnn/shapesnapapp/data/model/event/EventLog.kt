package com.orukunnn.shapesnapapp.data.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType {
    @SerialName("preset_impression")
    PRESET_IMPRESSION,

    @SerialName("preset_like")
    PRESET_LIKE,

    @SerialName("preset_unlike")
    PRESET_UNLIKE,

    @SerialName("preset_save")
    PRESET_SAVE,

    @SerialName("trend_item_click")
    TREND_ITEM_CLICK,
}

@Serializable
data class EventLog(
    val userId: String,
    val eventType: EventType,
    val presetId: String,
    val occurredAtEpochSeconds: Long,
    val platform: String,
    val appVersion: String,
    val sessionId: String,
    val rank: Int? = null,
)
