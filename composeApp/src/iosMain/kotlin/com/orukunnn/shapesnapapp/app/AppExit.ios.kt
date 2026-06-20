package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberAppExit(): () -> Unit =
    remember { {} }