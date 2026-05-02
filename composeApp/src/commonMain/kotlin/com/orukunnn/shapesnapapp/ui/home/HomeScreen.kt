package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val storedPresets = currentUser?.storage?.size ?: 0
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
                storagePresets = storedPresets,
                presets = state.presets,
                hasMore = state.hasMore,
                isLoadingMore = isLoadingMore,
                isRefreshing = isRefreshing,
                currentUid = uid,
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
    storagePresets: Int,
    presets: ImmutableList<Preset>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    isRefreshing: Boolean,
    currentUid: String?,
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
                storedPresets = storagePresets,
                titleColor = Color(0xFF62BCE7),
                containerColor = Color.White,
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
                modifier = Modifier.padding(horizontal = 16.dp)
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
    modifier: Modifier = Modifier,
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
//            contentPadding = PaddingValues(top = 12.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(presets, key = { it.id }) { preset ->
                PresetItem(
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

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeSuccessScreen(
        storagePresets = 3,
        presets = PresetsFactory.createPresets(),
        hasMore = true,
        isLoadingMore = false,
        isRefreshing = false,
        currentUid = null,
        onLoadMore = {},
        onRefresh = {},
        onToggleLike = {},
        onSave = {},
    )
}
