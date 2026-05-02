package com.orukunnn.shapesnapapp.core.util

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant

private val shapeSnapDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

internal actual fun formatInstantPlatform(instant: Instant): String =
    shapeSnapDateFormatter.format(
        java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds())
            .atZone(ZoneId.systemDefault()),
    )
