package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapButton
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.core.util.DateFormat
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.filled.FileDownload
import com.woowla.compose.icon.collections.tabler.tabler.filled.Heart
import com.woowla.compose.icon.collections.tabler.tabler.outline.Share
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.home_posted_prefix
import shapesnapv3.composeapp.generated.resources.preset_like
import shapesnapv3.composeapp.generated.resources.preset_liked
import shapesnapv3.composeapp.generated.resources.preset_save
import shapesnapv3.composeapp.generated.resources.preset_saved
import shapesnapv3.composeapp.generated.resources.preset_share

@Composable
internal fun PresetItem(
    preset: Preset,
    currentUid: String?,
    onToggleLike: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val liked = currentUid != null && currentUid in preset.likedUserIds
    val saved = currentUid != null && currentUid in preset.savedUserIds

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        val url = preset.imageUrl ?: preset.previewImageUrl
        if (!url.isNullOrBlank()) {
            AsyncImage(
                model = url,
                contentDescription = preset.displayName,
                modifier =
                    Modifier
                        .weight(2f)
                        .height(250.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .weight(2f)
                        .height(250.dp),
            ) {
                ColorPainter(Color.Gray)
            }
        }
        Spacer(Modifier.size(16.dp))
        Column(
            modifier = Modifier.weight(1.5f)
        ) {
            Text(
                text = preset.characterTagId,
                style = MaterialTheme.typography.titleMedium,
                color = ShapeSnapColors.TextSecondary,
            )
            Spacer(Modifier.size(8.dp))
            ShapeSnapButton(
                imageVector = Tabler.Filled.Heart,
                text = if (liked) {
                    stringResource(Res.string.preset_liked)
                } else {
                    stringResource(Res.string.preset_like)
                },
                enabled = !liked,
                color = ShapeSnapColors.Accent,
                onClick = onToggleLike,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
            ShapeSnapButton(
                imageVector = Tabler.Filled.FileDownload,
                text = if (saved) {
                    stringResource(Res.string.preset_saved)
                } else {
                    stringResource(Res.string.preset_save)
                },
                enabled = !saved,
                color = ShapeSnapColors.Brand,
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
            ShapeSnapButton(
                imageVector = Tabler.Outline.Share,
                text = stringResource(Res.string.preset_share),
                enabled = true,
                color = ShapeSnapColors.TextTertiary,
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
            Spacer(Modifier.size(8.dp))
            val postedDate = buildString {
                append(stringResource(Res.string.home_posted_prefix))
                append("\n")
                append(
                    DateFormat.convertShapeSnapDateFormat(
                        preset.createdAt
                    )
                )
            }
            Text(
                text = postedDate,
                style = MaterialTheme.typography.labelSmall,
                color = ShapeSnapColors.TextTertiary,
            )
        }
    }
}

@Preview
@Composable
private fun PresetItemPreview() {
    PresetItem(
        preset = PresetsFactory.createPreset(),
        currentUid = "",
        onToggleLike = {},
        onSave = {},
    )
}