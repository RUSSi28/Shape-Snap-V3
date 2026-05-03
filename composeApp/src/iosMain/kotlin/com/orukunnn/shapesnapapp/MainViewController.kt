@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.orukunnn.shapesnapapp

import androidx.compose.ui.window.ComposeUIViewController
import com.orukunnn.shapesnapapp.app.App
import com.orukunnn.shapesnapapp.di.appModule
import com.orukunnn.shapesnapapp.di.platformIosModule
import com.orukunnn.shapesnapapp.interop.bridge.shapesnap_ads_start_if_needed
import org.koin.core.context.startKoin

private var koinStarted = false

fun MainViewController() =
    ComposeUIViewController {
        shapesnap_ads_start_if_needed()
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
