package com.orukunnn.shapesnapapp.ui.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.filled.FileDownload
import com.woowla.compose.icon.collections.tabler.tabler.filled.Heart
import com.woowla.compose.icon.collections.tabler.tabler.outline.X
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.posts_delete
import shapesnapv3.composeapp.generated.resources.posts_like_count
import shapesnapv3.composeapp.generated.resources.posts_save_count

@Composable
internal fun PostPresetItem(
    preset: Preset,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            if (preset.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(ShapeSnapColors.TextTertiary),
                )
            } else {
                AsyncImage(
                    model = preset.imageUrl,
                    contentDescription = preset.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp),
            ) {
                PostMetric(
                    text = stringResource(
                        Res.string.posts_like_count,
                        preset.likedUserIds.size,
                    ),
                    icon = {
                        Icon(
                            imageVector = Tabler.Filled.Heart,
                            contentDescription = null,
                            tint = ShapeSnapColors.Accent,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                )
                PostMetric(
                    text = stringResource(
                        Res.string.posts_save_count,
                        preset.savedUserIds.size,
                    ),
                    icon = {
                        Icon(
                            imageVector = Tabler.Filled.FileDownload,
                            contentDescription = null,
                            tint = ShapeSnapColors.Brand,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                )
            }
        }
        SmallFloatingActionButton(
            onClick = onDeleteClick,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(36.dp),
        ) {
            Icon(
                imageVector = Tabler.Outline.X,
                contentDescription = stringResource(Res.string.posts_delete),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun PostMetric(
    text: String,
    icon: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = ShapeSnapColors.TextSecondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostPresetItemPreview() {
    PostPresetItem(
        preset = PresetsFactory.createPreset(),
        onDeleteClick = {},
        modifier = Modifier.padding(16.dp),
    )
}
