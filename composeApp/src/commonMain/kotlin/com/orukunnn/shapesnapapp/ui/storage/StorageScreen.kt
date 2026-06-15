package com.orukunnn.shapesnapapp.ui.storage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.storage_empty
import shapesnapv3.composeapp.generated.resources.storage_how_to_apply
import shapesnapv3.composeapp.generated.resources.storage_remove
import shapesnapv3.composeapp.generated.resources.storage_saved_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    onRequestToPopBackStack: () -> Unit,
    viewModel: StorageScreenViewModel = koinViewModel(),
) {
    val presets by viewModel.savedPresets.collectAsStateWithLifecycle()
    StorageScreen(
        presets = presets.toPersistentList(),
        onBackButtonClick = onRequestToPopBackStack,
        onRemoveButtonClick = viewModel::removePreset,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    presets: ImmutableList<StoredPreset>,
    onBackButtonClick: () -> Unit,
    onRemoveButtonClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            ShapeSnapAppBar(
                title = stringResource(Res.string.storage_saved_title),
                onArrowBackIconClick = onBackButtonClick,
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyGridState(),
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
       ) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.storage_how_to_apply),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                    Spacer(Modifier.size(200.dp))
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
                                    .background(ShapeSnapColors.Placeholder)
                                ) {
                                }
                            }
                            Button(
                                onClick = { onRemoveButtonClick(preset.id) },
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                            ) {
                                Text(stringResource(Res.string.storage_remove))
                            }
                        }
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
        onBackButtonClick = {},
        onRemoveButtonClick = {},
    )
}
