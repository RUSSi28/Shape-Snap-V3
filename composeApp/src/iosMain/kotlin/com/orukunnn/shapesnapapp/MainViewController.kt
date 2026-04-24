package com.orukunnn.shapesnapapp

import androidx.compose.ui.window.ComposeUIViewController
import com.orukunnn.shapesnapapp.app.App
import com.orukunnn.shapesnapapp.di.appModule
import com.orukunnn.shapesnapapp.di.platformIosModule
import org.koin.core.context.startKoin

private var koinStarted = false

fun MainViewController() =
    ComposeUIViewController {
        ensureIosKoinStarted()
        App()
    }

private fun ensureIosKoinStarted() {
    if (koinStarted) return
    startKoin {
        modules(appModule(), platformIosModule())
    }
    koinStarted = true
}
