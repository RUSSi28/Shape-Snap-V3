package com.orukunnn.shapesnapapp.ui.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.ui.common.LoadState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.posts_empty

@Composable
fun PostsScreen(
    currentUser: UserProfile,
    viewModel: PostsScreenViewModel = koinViewModel { parametersOf(currentUser.uid) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val postPendingDeletion by viewModel.postPendingDeletion.collectAsStateWithLifecycle()
    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()
    val deleteFailed by viewModel.deleteFailed.collectAsStateWithLifecycle()

    when (val s = state) {
        LoadState.Idle, LoadState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
        is LoadState.Success -> {
            if (s.data.isEmpty()) {
                Text(stringResource(Res.string.posts_empty))
            } else {
                val presetsByCharacter = s.data.groupBy { it.characterTagId }
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = presetsByCharacter.entries.toList(),
                        key = { it.key },
                    ) { (characterTagId, presets) ->
                        PostCharacterSection(
                            characterTagId = characterTagId,
                            presets = presets,
                            onDeleteClick = viewModel::requestPostDeletion,
                        )
                    }
                }
            }
        }
    }

    if (postPendingDeletion != null) {
        DeletePostConfirmDialog(
            isDeleting = isDeleting,
            deleteFailed = deleteFailed,
            onConfirm = viewModel::confirmPostDeletion,
            onDismiss = viewModel::dismissPostDeletion,
        )
    }
}
