package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.data.model.trend.TrendPreset
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrendsLazyRow(
    trendPresets: ImmutableList<TrendPreset>,
    onItemClick: (presetId: String) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(items = trendPresets, key = { it.preset.id }) { trendPreset ->
            Box {
                Card(
                    onClick = { onItemClick(trendPreset.preset.id) },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    AsyncImage(
                        model = trendPreset.preset.imageUrl,
                        contentDescription = trendPreset.preset.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(180.dp),
                    )
                }
                Surface(
                    color = ShapeSnapColors.Brand,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomEnd = 12.dp),
                    modifier = Modifier.align(Alignment.TopStart),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Text(
                            text = "${trendPreset.rank}",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
