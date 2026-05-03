@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import com.orukunnn.shapesnapapp.interop.bridge.shapesnap_ads_create_test_banner
import platform.UIKit.UIView

@Composable
actual fun AdBannerView(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxWidth()
            .height(50.dp),
    ) {
        UIKitView<UIView>(
            factory = {
                shapesnap_ads_create_test_banner()
            },
            modifier = Modifier.fillMaxSize().align(Alignment.BottomCenter),
        )
    }
}
