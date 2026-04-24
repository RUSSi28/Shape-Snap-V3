package com.orukunnn.shapesnapapp.core.util

import co.touchlab.kermit.Logger

object AppLogger {
    private val log = Logger.withTag("ShapeSnap")

    fun d(message: String) = log.d(message)

    fun i(message: String) = log.i(message)

    fun w(message: String, throwable: Throwable? = null) = if (throwable != null) log.w(throwable) { message } else log.w { message }

    fun e(message: String, throwable: Throwable? = null) = if (throwable != null) log.e(throwable) { message } else log.e { message }
}
