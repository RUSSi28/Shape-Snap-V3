package com.orukunnn.shapesnapapp.core.util

import kotlinx.coroutines.CoroutineDispatcher

expect object AppDispatchers {
    val io: CoroutineDispatcher
}
