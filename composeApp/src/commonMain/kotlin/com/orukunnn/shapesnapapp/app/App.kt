package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orukunnn.shapesnapapp.data.repository.auth.AuthRepository
import com.orukunnn.shapesnapapp.ui.login.LoginScreen
import org.koin.compose.koinInject

@Composable
fun App() {
    AppTheme {
        val authRepository: AuthRepository = koinInject()
        val user by authRepository.currentUser.collectAsStateWithLifecycle()
        val isAuthReady by authRepository.isAuthReady.collectAsStateWithLifecycle()

        when {
            user != null -> MainScreen()
            isAuthReady -> LoginScreen()
            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
