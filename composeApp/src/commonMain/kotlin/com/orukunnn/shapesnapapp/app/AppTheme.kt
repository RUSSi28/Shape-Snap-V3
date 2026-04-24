package com.orukunnn.shapesnapapp.app

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val LightColors =
    lightColorScheme(
        primary = Color(0xFF6750A4),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEADDFF),
        secondary = Color(0xFF625B71),
    )

private val DarkColors =
    darkColorScheme(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFF4F378B),
        secondary = Color(0xFFCCC2DC),
    )

@Immutable
data class ShapeSnapColorScheme(val material: ColorScheme)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, typography = MaterialTheme.typography, content = content)
}
