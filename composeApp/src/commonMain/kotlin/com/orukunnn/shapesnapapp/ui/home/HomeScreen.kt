package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.orukunnn.shapesnapapp.core.util.DateFormat
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.orukunnn.shapesnapapp.ui.common.LimitReachedDialog
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapHomeAppBar
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.home_empty
import shapesnapv3.composeapp.generated.resources.home_posted_prefix
import shapesnapv3.composeapp.generated.resources.home_title
import shapesnapv3.composeapp.generated.resources.preset_like
import shapesnapv3.composeapp.generated.resources.preset_menu
import shapesnapv3.composeapp.generated.resources.preset_saved

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val showLimit by viewModel.showLimitReachedDialog.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val uid = currentUser?.uid
    val isLoggedIn = currentUser != null

    when (val state = homeState) {
        HomeUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Error -> {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is HomeUiState.Success -> {
            HomeSuccessScreen(
                title = stringResource(Res.string.home_title),
                presets = state.presets,
                hasMore = state.hasMore,
                isLoadingMore = isLoadingMore,
                isRefreshing = isRefreshing,
                currentUid = uid,
                isLoggedIn = isLoggedIn,
                onMenuClick = onMenuClick,
                onLogoutClick = onLogoutClick,
                onLoginClick = { viewModel.signInWithGoogle() },
                onLoadMore = { viewModel.loadMore() },
                onRefresh = { viewModel.refreshPresets() },
                onToggleLike = { viewModel.toggleLike(it) },
                onSave = { viewModel.saveToStorage(it) },
            )
        }
    }

    if (showLimit) {
        LimitReachedDialog(onDismiss = { viewModel.dismissLimitDialog() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSuccessScreen(
    title: String,
    presets: ImmutableList<Preset>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    isRefreshing: Boolean,
    currentUid: String?,
    isLoggedIn: Boolean,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onToggleLike: (String) -> Unit,
    onSave: (String) -> Unit,
) {
    val pullState = rememberPullToRefreshState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ShapeSnapHomeAppBar(
                title = title,
                isLoggedIn = isLoggedIn,
                onMenuClick = onMenuClick,
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick,
                scrollBehavior = null,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            HomeScreenContent(
                presets = presets,
                hasMore = hasMore,
                isLoadingMore = isLoadingMore,
                currentUid = currentUid,
                onLoadMore = onLoadMore,
                onToggleLike = onToggleLike,
                onSave = onSave,
            )
        }
    }
}

@Composable
private fun HomeScreenContent(
    presets: ImmutableList<Preset>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    currentUid: String?,
    onLoadMore: () -> Unit,
    onToggleLike: (String) -> Unit,
    onSave: (String) -> Unit,
) {
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState, hasMore) {
        snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.distinctUntilChanged()
            .collect { lastVisible ->
                val total = gridState.layoutInfo.totalItemsCount
                if (lastVisible != null && total > 0 && hasMore && lastVisible >= total - 3) {
                    onLoadMore()
                }
            }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        if (presets.isEmpty()) {
            Text(
                stringResource(Res.string.home_empty),
                modifier = Modifier.padding(16.dp),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(340.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 140.dp),
            ) {
                items(presets, key = { it.id }) { preset ->
                    PresetCard(
                        preset = preset,
                        currentUid = currentUid,
                        onToggleLike = { onToggleLike(preset.id) },
                        onSave = { onSave(preset.id) },
                    )
                }
                if (isLoadingMore) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetCard(
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

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeSuccessScreen(
        title = stringResource(Res.string.home_title),
        presets = PresetsFactory.sample(),
        hasMore = true,
        isLoadingMore = false,
        isRefreshing = false,
        currentUid = null,
        isLoggedIn = false,
        onMenuClick = {},
        onLogoutClick = {},
        onLoginClick = {},
        onLoadMore = {},
        onRefresh = {},
        onToggleLike = {},
        onSave = {},
    )
}
