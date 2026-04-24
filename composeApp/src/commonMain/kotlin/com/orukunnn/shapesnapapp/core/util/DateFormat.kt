package com.orukunnn.shapesnapapp.core.util

import kotlin.time.Instant

object DateFormat {
    fun formatInstant(instant: Instant): String = formatInstantPlatform(instant)

    fun convertShapeSnapDateFormat(instant: Instant): String = formatInstant(instant)
}

internal expect fun formatInstantPlatform(instant: Instant): String
