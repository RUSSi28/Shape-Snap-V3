package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.orukunnn.shapesnapapp.ui.common.AppExitConfirmDialog
import com.orukunnn.shapesnapapp.ui.login.LoginScreen

fun NavGraphBuilder.loginNavGraph() {
    composable<LoginDestination> {
        LoginShell()
    }
}

@Composable
private fun LoginShell() {
    var showExitConfirm by remember { mutableStateOf(false) }
    val exitApp = rememberAppExit()

    AppBackHandler(onBack = { showExitConfirm = true })

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }

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
