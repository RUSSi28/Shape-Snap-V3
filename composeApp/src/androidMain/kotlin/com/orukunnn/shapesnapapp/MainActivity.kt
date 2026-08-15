package com.orukunnn.shapesnapapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.orukunnn.shapesnapapp.app.App
import com.orukunnn.shapesnapapp.domain.DeepLinkHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(Color.BLACK),
        )
        super.onCreate(savedInstanceState)
        receiveDeepLink(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        receiveDeepLink(intent)
    }

    private fun receiveDeepLink(intent: android.content.Intent?) {
        intent?.dataString?.let(DeepLinkHandler::receive)
    }
}
