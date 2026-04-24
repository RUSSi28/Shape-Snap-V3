package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.core.util.DateFormat
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.home_posted_prefix
import shapesnapv3.composeapp.generated.resources.preset_like
import shapesnapv3.composeapp.generated.resources.preset_menu
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        val url = preset.imageUrl ?: preset.previewImageUrl
        if (!url.isNullOrBlank()) {
            AsyncImage(
                model = url,
                contentDescription = preset.displayName,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),
            ) {
                ColorPainter(Color.Gray)
            }
        }
        Text(preset.displayName, style = MaterialTheme.typography.titleMedium)
        preset.description?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            stringResource(Res.string.home_posted_prefix) + DateFormat.convertShapeSnapDateFormat(
                preset.createdAt
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onToggleLike) {
                Text(
                    stringResource(Res.string.preset_like),
                    color = if (liked) Color(0xFFE91E63) else MaterialTheme.colorScheme.primary,
                )
            }
            TextButton(onClick = onSave) {
                Text(
                    stringResource(Res.string.preset_saved),
                    color = if (saved) Color(0xFF005D53) else MaterialTheme.colorScheme.primary,
                )
            }
            Box {
                TextButton(onClick = { menuOpen = true }) {
                    Text(stringResource(Res.string.preset_menu))
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.preset_like)) },
                        onClick = {
                            menuOpen = false
                            onToggleLike()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.preset_saved)) },
                        onClick = {
                            menuOpen = false
                            onSave()
                        },
                    )
                }
            }
        }
    }
}