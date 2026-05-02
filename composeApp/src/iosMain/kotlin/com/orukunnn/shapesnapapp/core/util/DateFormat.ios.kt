package com.orukunnn.shapesnapapp.core.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import kotlin.time.Instant

private const val appleReferenceDateEpochSeconds = 978_307_200.0

private fun shapeSnapDateFormatter(): NSDateFormatter = NSDateFormatter().apply {
    dateFormat = "yyyy-MM-dd HH:mm"
}

internal actual fun formatInstantPlatform(instant: Instant): String =
    shapeSnapDateFormatter().stringFromDate(
        NSDate(
            timeIntervalSinceReferenceDate =
                instant.toEpochMilliseconds().toDouble() / 1000.0 - appleReferenceDateEpochSeconds,
        ),
    )
