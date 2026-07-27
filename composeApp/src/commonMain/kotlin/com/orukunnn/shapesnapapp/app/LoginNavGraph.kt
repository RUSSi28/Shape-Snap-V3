package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.orukunnn.shapesnapapp.ui.login.LoginScreen

fun NavGraphBuilder.loginNavGraph() {
    composable<LoginDestination> {
        LoginShell()
    }
}

@Composable
private fun LoginShell() {
    AppExitBackHandler()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
