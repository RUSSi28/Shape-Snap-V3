package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.orukunnn.shapesnapapp.ui.common.AppExitConfirmDialog

/**
 * システム戻るで終了確認ダイアログを出し、確定時のみアプリを終了する。
 */
@Composable
fun AppExitBackHandler() {
    var showExitConfirm by remember { mutableStateOf(false) }
    val exitApp = rememberAppExit()

    AppBackHandler(onBack = { showExitConfirm = true })

    if (showExitConfirm) {
        AppExitConfirmDialog(
            onExitConfirm = {
                showExitConfirm = false
                exitApp()
            },
            onDismiss = { showExitConfirm = false },
        )
    }
}
