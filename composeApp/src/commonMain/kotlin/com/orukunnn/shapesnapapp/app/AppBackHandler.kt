package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppBackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
) {
    BackHandler(
        enabled = enabled,
        onBack = onBack,
    )
}
