package com.orukunnn.shapesnapapp.domain

import com.orukunnn.shapesnapapp.core.platform.Platform
import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.datasource.EventDatasource
import com.orukunnn.shapesnapapp.data.model.event.EventLog
import com.orukunnn.shapesnapapp.data.model.event.EventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Clock

class EventLogger(
    private val eventDatasource: EventDatasource,
    private val scope: CoroutineScope,
) {
    private val sessionId =
        "${Clock.System.now().epochSeconds}-${Random.nextLong().toString(16)}"

    fun logPresetLike(
        userId: String,
        presetId: String,
        isLike: Boolean,
    ) {
        log(
            userId = userId,
            presetId = presetId,
            eventType = if (isLike) EventType.PRESET_LIKE else EventType.PRESET_UNLIKE,
        )
    }

    fun logPresetSave(
        userId: String,
        presetId: String,
    ) {
        log(userId, presetId, EventType.PRESET_SAVE)
    }

    private fun log(
        userId: String,
        presetId: String,
        eventType: EventType,
    ) {
        if (userId.isBlank() || presetId.isBlank()) {
            AppLogger.w("イベント送信をスキップ: ユーザーIDまたはプリセットIDがありません")
            return
        }
        scope.launch {
            val event = EventLog(
                userId = userId,
                eventType = eventType,
                presetId = presetId,
                occurredAtEpochSeconds = Clock.System.now().epochSeconds,
                platform = Platform.name.substringBefore(' ').lowercase(),
                appVersion = APP_VERSION,
                sessionId = sessionId,
            )
            AppLogger.d("イベントログ: $event")
            eventDatasource
                .logEvent(
                    EventLog(
                        userId = userId,
                        eventType = eventType,
                        presetId = presetId,
                        occurredAtEpochSeconds = Clock.System.now().epochSeconds,
                        platform = Platform.name.substringBefore(' ').lowercase(),
                        appVersion = APP_VERSION,
                        sessionId = sessionId,
                    ),
                ).onFailure { error ->
                    AppLogger.w("イベント送信に失敗 type=$eventType presetId=$presetId", error)
                }
        }
    }

    private companion object {
        const val APP_VERSION = "1.0.0"
    }
}
