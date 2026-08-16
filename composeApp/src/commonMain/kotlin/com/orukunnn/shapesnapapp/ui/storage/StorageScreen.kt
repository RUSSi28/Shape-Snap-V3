package com.orukunnn.shapesnapapp.ui.storage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapButton
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.outline.Trash
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.storage_empty
import shapesnapv3.composeapp.generated.resources.storage_how_to_apply
import shapesnapv3.composeapp.generated.resources.storage_how_to_apply_title
import shapesnapv3.composeapp.generated.resources.storage_remove

@Composable
fun StorageScreen(
    viewModel: StorageScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        StorageUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is StorageUiState.Success -> {
            StorageScreen(
                presets = currentState.presets,
                onRemoveButtonClick = viewModel::requestRemoval,
            )
            if (currentState.presetPendingRemoval != null) {
                RemoveStorageConfirmDialog(
                    isRemoving = currentState.isRemoving,
                    removeFailed = currentState.removeFailed,
                    onConfirm = viewModel::confirmRemoval,
                    onDismiss = viewModel::dismissRemoval,
                )
            }
        }
    }
}

@Composable
fun StorageScreen(
    presets: ImmutableList<StoredPreset>,
    onRemoveButtonClick: (StoredPreset) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = rememberLazyGridState(),
        contentPadding = PaddingValues(bottom = 8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item(
            key = "how_to_apply_text",
            span = { GridItemSpan(2) }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.storage_how_to_apply_title),
                        fontSize = 16.sp,
                    )
                    Text(
                        text = stringResource(Res.string.storage_how_to_apply),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                    )
                }
                // TODO: 説明をわかりやすくするための画像を置く
            }
        }
        if (presets.isEmpty()) {
            item("no_presets_text") {
                Text(
                    stringResource(Res.string.storage_empty),
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        } else {
            items(presets, key = { it.id }) { preset ->
                Card(Modifier.fillMaxWidth()) {
                    Column {
                        if (!preset.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = preset.imageUrl,
                                contentDescription = preset.id,
                                modifier = Modifier.fillMaxWidth().height(140.dp),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .background(ShapeSnapColors.Placeholder),
                            )
                        }
                        ShapeSnapButton(
                            imageVector = Tabler.Outline.Trash,
                            text = stringResource(Res.string.storage_remove),
                            enabled = true,
                            color = ShapeSnapColors.TextTertiary,
                            onClick = { onRemoveButtonClick(preset) },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun StorageScreenPreview() {
    StorageScreen(
        presets = listOf(
            StoredPreset(id = "id1", imageUrl = "https://example.com/image.png"),
            StoredPreset(id = "id2", imageUrl = null),
            StoredPreset(id = "id3", imageUrl = null),
        ).toPersistentList(),
        onRemoveButtonClick = {},
    )
}
