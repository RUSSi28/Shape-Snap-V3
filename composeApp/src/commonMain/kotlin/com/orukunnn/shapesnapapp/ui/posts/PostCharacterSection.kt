package com.orukunnn.shapesnapapp.ui.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.posts_unknown_character

@Composable
internal fun PostCharacterSection(
    characterTagId: String,
    presets: List<Preset>,
    onDeleteClick: (Preset) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        Text(
            text = characterTagId.ifBlank {
                stringResource(Res.string.posts_unknown_character)
            },
            style = MaterialTheme.typography.titleMedium,
            color = ShapeSnapColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = presets,
                key = Preset::id,
            ) { preset ->
                PostPresetItem(
                    preset = preset,
                    onDeleteClick = { onDeleteClick(preset) },
                    modifier = Modifier.width(180.dp),
                )
            }
        }
    }
}
