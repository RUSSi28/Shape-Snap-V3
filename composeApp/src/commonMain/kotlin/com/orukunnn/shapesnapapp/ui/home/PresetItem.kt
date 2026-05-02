package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapButton
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
import shapesnapv3.composeapp.generated.resources.preset_saved

@Composable
internal fun PresetItem(
    preset: Preset,
    currentUid: String?,
    onToggleLike: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuOpen by remember { mutableStateOf(false) }
    val liked = currentUid != null && currentUid in preset.likedUserIds
    val saved = currentUid != null && currentUid in preset.savedUserIds

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
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
//                            .fillMaxWidth()
                            .weight(2f)
                            .height(250.dp),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier =
                        Modifier
//                            .fillMaxWidth()
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
                    color = Color.DarkGray,
                )
                Spacer(Modifier.size(8.dp))
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
                ShapeSnapButton(
                    imageVector = Tabler.Filled.Heart,
                    text = if (liked) {
                        "liked"
                    } else {
                        stringResource(Res.string.preset_like)
                    },
                    enabled = !liked,
                    color = Color(0xFFEA6399),
                    onClick = onToggleLike,
//                        modifier = Modifier.weight(1f)
                )
//                Spacer(Modifier.size(4.dp))
                ShapeSnapButton(
                    imageVector = Tabler.Filled.FileDownload,
                    text = if (saved) {
                        stringResource(Res.string.preset_saved)
                    } else {
                        "Save"
                    },
                    enabled = !saved,
                    color = Color(0xFF62BCE7),
                    onClick = onSave,
//                        modifier = Modifier.weight(1f)
                )
//                Spacer(Modifier.size(4.dp))
                ShapeSnapButton(
                    imageVector = Tabler.Outline.Share,
                    text = "Share",
                    enabled = true,
                    color = Color.Gray,
                    onClick = {  },
//                        modifier = Modifier.weight(1f)
                )
//                IconButton(onClick = { }) {
//                    Icon(
//                        imageVector = Tabler.Outline.Share,
//                        contentDescription = null,
//                        tint = Color.Gray,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
                Spacer(Modifier.size(8.dp))
                Text(
                    stringResource(Res.string.home_posted_prefix) + DateFormat.convertShapeSnapDateFormat(
                        preset.createdAt
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
//                }
            }
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