package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun TrendsLazyRow(
    presets: ImmutableList<Preset>,
) {
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = presets, key = { it.id }) { preset ->
            if (presets[0].id == preset.id) {
                Spacer(Modifier.size(16.dp))
            }
            Card (
                shape = RoundedCornerShape(16.dp),
            ){
                AsyncImage(
                    model = preset.imageUrl,
                    contentDescription = preset.id,
                    modifier = Modifier.size(150.dp)
                )

            }
        }
    }
}