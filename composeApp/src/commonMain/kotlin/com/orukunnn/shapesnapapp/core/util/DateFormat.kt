package com.orukunnn.shapesnapapp.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormat {
    fun formatInstant(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
        val local = instant.toLocalDateTime(timeZone)
        return "${local.year}-${local.monthNumber.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')} " +
            "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}"
    }

    fun convertShapeSnapDateFormat(instant: Instant): String = formatInstant(instant)
}
