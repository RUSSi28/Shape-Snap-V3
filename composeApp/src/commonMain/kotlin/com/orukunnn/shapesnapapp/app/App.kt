package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.ui.login.LoginScreen
import org.koin.compose.koinInject

@Composable
fun App() {
    AppTheme {
        val authRepository: AuthRepository = koinInject()
        val user by authRepository.currentUser.collectAsState(initial = null)

        if (user == null) {
            LoginScreen()
        } else {
            MainScreen()
        }
    }
}
