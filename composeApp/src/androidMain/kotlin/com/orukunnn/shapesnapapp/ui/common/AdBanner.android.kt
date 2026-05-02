package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
actual fun AdBannerView(modifier: Modifier) {
    Box(modifier = modifier
        .background(Color.Black)
        .fillMaxWidth()
        .height(50.dp)
    ) {
        AndroidView(
            factory = { context ->
                val adView = AdView(context)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = "ca-app-pub-3940256099942544/6300978111" // テストID
                adView.loadAd(AdRequest.Builder().build())
                adView
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
