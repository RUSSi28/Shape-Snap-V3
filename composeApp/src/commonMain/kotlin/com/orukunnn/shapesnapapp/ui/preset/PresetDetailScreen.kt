package com.orukunnn.shapesnapapp.ui.preset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.app.ShapeSnapButton
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.filled.FileDownload
import com.woowla.compose.icon.collections.tabler.tabler.filled.Heart
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PresetDetailScreen(
    presetId: String,
    currentUser: UserProfile,
    onBack: () -> Unit,
    viewModel: PresetDetailViewModel = koinViewModel {
        parametersOf(presetId, currentUser)
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        PresetDetailUiState.Loading -> {
            LoadingContent()
        }
        PresetDetailUiState.NotFound -> {
            MessageContent(
                message = "プリセットが見つかりませんでした。",
                onBack = onBack,
            )
        }
        is PresetDetailUiState.Error -> {
            MessageContent(
                message = currentState.message,
                onBack = onBack,
                onRetry = viewModel::loadPreset,
            )
        }
        is PresetDetailUiState.Success -> {
            val preset = currentState.preset
            val liked = currentUser.uid in preset.likedUserIds
            val saved = currentUser.uid in preset.savedUserIds
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {
                preset.imageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = preset.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(280.dp),
                    )
                }
                Text(preset.displayName, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = preset.characterTagId,
                    style = MaterialTheme.typography.titleMedium,
                    color = ShapeSnapColors.TextSecondary,
                )
                preset.description?.takeIf(String::isNotBlank)?.let { description ->
                    Text(description, style = MaterialTheme.typography.bodyLarge)
                }
                ShapeSnapButton(
                    imageVector = Tabler.Filled.Heart,
                    text = if (liked) "いいね済み" else "いいね",
                    enabled = true,
                    color = if (liked) ShapeSnapColors.Accent else ShapeSnapColors.TextTertiary,
                    onClick = viewModel::toggleLike,
                    modifier = Modifier.fillMaxWidth(),
                )
                ShapeSnapButton(
                    imageVector = Tabler.Filled.FileDownload,
                    text = if (saved) "保存済み" else "保存",
                    enabled = true,
                    color = if (saved) ShapeSnapColors.Brand else ShapeSnapColors.TextTertiary,
                    onClick = viewModel::toggleSave,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessageContent(
    message: String,
    onBack: () -> Unit,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.size(16.dp))
        onRetry?.let {
            Button(onClick = it) {
                Text("再試行")
            }
            Spacer(Modifier.size(8.dp))
        }
        Button(onClick = onBack) {
            Text("戻る")
        }
    }
}
