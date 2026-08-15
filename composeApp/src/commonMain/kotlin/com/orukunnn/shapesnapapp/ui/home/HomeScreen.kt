package com.orukunnn.shapesnapapp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.preset.PresetsFactory
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.domain.PresetShareLink
import com.orukunnn.shapesnapapp.platform.rememberShareText
import com.orukunnn.shapesnapapp.ui.common.LimitReachedDialog
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.outline.Crown
import com.woowla.compose.icon.collections.tabler.tabler.outline.Timeline
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.home_empty
import shapesnapv3.composeapp.generated.resources.home_section_presets
import shapesnapv3.composeapp.generated.resources.home_section_trends

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: UserProfile,
    onNavigateToPresetDetail: (String) -> Unit,
    viewModel: HomeScreenViewModel = koinViewModel { parametersOf(currentUser) },
) {
    val homeState by viewModel.homeState.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val showLimit by viewModel.showLimitReachedDialog.collectAsStateWithLifecycle()
    val shareText = rememberShareText()

    val uid = currentUser.uid

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
                presets = state.presets,
                hasMore = state.hasMore,
                isLoadingMore = isLoadingMore,
                isRefreshing = isRefreshing,
                currentUid = uid,
                onLoadMore = { viewModel.loadMore() },
                onRefresh = { viewModel.refreshPresets() },
                onToggleLike = { viewModel.toggleLike(it) },
                onSave = { viewModel.toggleSave(it) },
                onNavigateToPresetDetail = onNavigateToPresetDetail,
                onSharePreset = { preset ->
                    PresetShareLink.create(preset.id)?.let { link ->
                        shareText("${preset.displayName}\n$link")
                    }
                },
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
    presets: ImmutableList<Preset>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    isRefreshing: Boolean,
    currentUid: String?,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onToggleLike: (String) -> Unit,
    onSave: (String) -> Unit,
    onNavigateToPresetDetail: (String) -> Unit,
    onSharePreset: (Preset) -> Unit,
) {
    val pullState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullState,
        modifier = Modifier.fillMaxSize(),
    ) {
        HomeScreenContent(
            presets = presets,
            hasMore = hasMore,
            isLoadingMore = isLoadingMore,
            currentUid = currentUid,
            onLoadMore = onLoadMore,
            onToggleLike = onToggleLike,
            onSave = onSave,
            onNavigateToPresetDetail = onNavigateToPresetDetail,
            onSharePreset = onSharePreset,
        )
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
    onNavigateToPresetDetail: (String) -> Unit,
    onSharePreset: (Preset) -> Unit,
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
            modifier = modifier.fillMaxSize()
        ) {
            item(
                key = "TrendsHeading",
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = Tabler.Outline.Crown,
                        contentDescription = null,
                        tint = ShapeSnapColors.TextSecondary,
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = stringResource(Res.string.home_section_trends),
                        fontSize = 18.sp,
                        color = ShapeSnapColors.TextSecondary,
                    )
                }
            }
            item(
                key = "TrendItem",
                span = { GridItemSpan(maxLineSpan) },
            ) {
                TrendsLazyRow(
                    presets = presets,
                )
            }
            item(
                key = "PresetsHeading",
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                ) {
                    Icon(
                        imageVector = Tabler.Outline.Timeline,
                        contentDescription = null,
                        tint = ShapeSnapColors.TextSecondary,
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = stringResource(Res.string.home_section_presets),
                        fontSize = 18.sp,
                        color = ShapeSnapColors.TextSecondary,
                    )
                }
            }
            items(presets, key = { it.id }) { preset ->
                PresetItem(
                    preset = preset,
                    currentUid = currentUid,
                    onClickLike = { onToggleLike(preset.id) },
                    onClickSave = { onSave(preset.id) },
                    onOpenDetail = { onNavigateToPresetDetail(preset.id) },
                    onClickShare = { onSharePreset(preset) },
                    modifier = Modifier.padding(
                        vertical = 16.dp,
                        horizontal = 8.dp
                    )
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
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

@Preview(name = "Portrait", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Landscape", widthDp = 800, heightDp = 360, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeSuccessScreen(
        presets = PresetsFactory.createPresets(),
        hasMore = true,
        isLoadingMore = false,
        isRefreshing = false,
        currentUid = null,
        onLoadMore = {},
        onRefresh = {},
        onToggleLike = {},
        onSave = {},
        onNavigateToPresetDetail = {},
        onSharePreset = {},
    )
}
