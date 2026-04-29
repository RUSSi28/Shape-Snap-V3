package com.orukunnn.shapesnapapp.ui.storage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.storage_empty
import shapesnapv3.composeapp.generated.resources.storage_remove
import shapesnapv3.composeapp.generated.resources.storage_saved_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    onRequestToPopBackStack: () -> Unit,
    viewModel: StorageScreenViewModel = koinViewModel(),
) {
    val ids by viewModel.savedPresetIds.collectAsState()

    Scaffold(
        topBar = {
            ShapeSnapAppBar(
                title = stringResource(Res.string.storage_saved_title),
                onArrowBackIconClick = onRequestToPopBackStack,
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Card(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "アバターへの適用方法",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
            if (ids.isEmpty()) {
                Text(
                    stringResource(Res.string.storage_empty),
                    modifier = Modifier.padding(top = 16.dp),
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 12.dp),
                ) {
                    items(ids, key = { it }) { id ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(id, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                                Button(onClick = { viewModel.removePreset(id) }) {
                                    Text(stringResource(Res.string.storage_remove))
                                }
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
    StorageScreen(onRequestToPopBackStack = {})
}
