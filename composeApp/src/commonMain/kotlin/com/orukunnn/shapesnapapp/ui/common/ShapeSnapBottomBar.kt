package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.orukunnn.shapesnapapp.app.HomeDestination
import com.orukunnn.shapesnapapp.app.PostsDestination
import com.orukunnn.shapesnapapp.app.ProfileDestination
import com.orukunnn.shapesnapapp.app.SearchDestination
import com.orukunnn.shapesnapapp.app.StorageDestination
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.filled.Apps
import com.woowla.compose.icon.collections.tabler.tabler.filled.Archive
import com.woowla.compose.icon.collections.tabler.tabler.filled.ArrowBadgeDown
import com.woowla.compose.icon.collections.tabler.tabler.filled.CircleLetterA
import com.woowla.compose.icon.collections.tabler.tabler.filled.FileUpload
import com.woowla.compose.icon.collections.tabler.tabler.filled.Files
import com.woowla.compose.icon.collections.tabler.tabler.filled.Home
import com.woowla.compose.icon.collections.tabler.tabler.filled.Paint
import com.woowla.compose.icon.collections.tabler.tabler.filled.Salad
import com.woowla.compose.icon.collections.tabler.tabler.outline.Search
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.bottom_home
import shapesnapv3.composeapp.generated.resources.bottom_post
import shapesnapv3.composeapp.generated.resources.bottom_profile
import shapesnapv3.composeapp.generated.resources.bottom_search
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
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        color = Color.White.copy(alpha = 0.95f),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
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
            )
            BottomBarItem(
                label = stringResource(Res.string.bottom_post),
                selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(PostsDestination::class) } ==
                        true,
                icon = Tabler.Filled.FileUpload,
                onClick = onNavigatePost,
            )
            BottomBarItem(
                label = stringResource(Res.string.bottom_home),
                selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(HomeDestination::class) } ==
                        true,
                icon = Tabler.Filled.Home,
                onClick = onNavigateHome,
            )
            BottomBarItem(
                label = stringResource(Res.string.bottom_storage),
                selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(StorageDestination::class) } ==
                        true,
                icon = Tabler.Filled.Files,
                onClick = onNavigateStorage,
            )
            BottomBarItem(
                label = stringResource(Res.string.bottom_profile),
                selected =
                    currentDestination?.hierarchy?.any { it.hasRoute(ProfileDestination::class) } ==
                        true,
                icon = Tabler.Filled.CircleLetterA,
                onClick = onNavigateProfile,
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val color =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
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
    MaterialTheme{
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