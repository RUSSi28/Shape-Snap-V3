package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.orukunnn.shapesnapapp.app.HomeDestination
import com.orukunnn.shapesnapapp.app.PostsDestination
import com.orukunnn.shapesnapapp.app.SearchDestination
import com.orukunnn.shapesnapapp.app.SettingsDestination
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.app.StorageDestination
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.filled.FileUpload
import com.woowla.compose.icon.collections.tabler.tabler.filled.Files
import com.woowla.compose.icon.collections.tabler.tabler.filled.Home
import com.woowla.compose.icon.collections.tabler.tabler.filled.Settings
import com.woowla.compose.icon.collections.tabler.tabler.outline.Search
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.bottom_home
import shapesnapv3.composeapp.generated.resources.bottom_post
import shapesnapv3.composeapp.generated.resources.bottom_search
import shapesnapv3.composeapp.generated.resources.bottom_settings
import shapesnapv3.composeapp.generated.resources.bottom_storage

@Composable
fun ShapeSnapBottomBar(
    currentDestination: NavDestination?,
    onNavigateSearch: () -> Unit,
    onNavigatePost: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateStorage: () -> Unit,
    onNavigateProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ShapeSnapColors.Surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottomBarItem(
            label = stringResource(Res.string.bottom_search),
            selected =
                currentDestination?.hierarchy?.any { it.hasRoute(SearchDestination::class) } ==
                        true,
            icon = Tabler.Outline.Search,
            onClick = onNavigateSearch,
            modifier = Modifier.weight(1f),
        )
        BottomBarItem(
            label = stringResource(Res.string.bottom_post),
            selected =
                currentDestination?.hierarchy?.any { it.hasRoute(PostsDestination::class) } ==
                        true,
            icon = Tabler.Filled.FileUpload,
            onClick = onNavigatePost,
            modifier = Modifier.weight(1f),
        )
        BottomBarItem(
            label = stringResource(Res.string.bottom_home),
            selected =
                currentDestination?.hierarchy?.any { it.hasRoute(HomeDestination::class) } ==
                        true,
            icon = Tabler.Filled.Home,
            onClick = onNavigateHome,
            modifier = Modifier.weight(1f),
        )
        BottomBarItem(
            label = stringResource(Res.string.bottom_storage),
            selected =
                currentDestination?.hierarchy?.any { it.hasRoute(StorageDestination::class) } ==
                        true,
            icon = Tabler.Filled.Files,
            onClick = onNavigateStorage,
            modifier = Modifier.weight(1f),
        )
        BottomBarItem(
            label = stringResource(Res.string.bottom_settings),
            selected =
                currentDestination?.hierarchy?.any { it.hasRoute(SettingsDestination::class) } ==
                        true,
            icon = Tabler.Filled.Settings,
            onClick = onNavigateProfile,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color =
        if (selected) {
            ShapeSnapColors.Brand
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = color,
        )
    }
}

@Preview
@Composable
private fun ShapeSnapBottomBarPreview() {
    MaterialTheme {
        ShapeSnapBottomBar(
            currentDestination = null,
            onNavigateSearch = {},
            onNavigatePost = {},
            onNavigateHome = {},
            onNavigateStorage = {},
            onNavigateProfile = {},
        )
    }
}