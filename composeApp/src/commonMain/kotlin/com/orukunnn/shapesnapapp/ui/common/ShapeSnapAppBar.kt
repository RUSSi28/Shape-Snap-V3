package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.filled.User
import com.woowla.compose.icon.collections.tabler.tabler.outline.User
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.home_title

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShapeSnapHomeAppBar(
    title: String,
    isLoggedIn: Boolean,
    onMenuClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
//        navigationIcon = {
//            IconButton(onClick = onMenuClick) {
//                Text("☰", style = MaterialTheme.typography.titleLarge)
//            }
//        },
//        actions = {
//            if (isLoggedIn) {
//                IconButton(
//                    imageVector = Tabler.Filled.User,
//                    onClick = onLogoutClick,
//                )
//            } else {
//                IconButton(
//                    imageVector = Tabler.Outline.User,
//                    onClick = onLoginClick,
//                )
//            }
//        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@Composable
fun IconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color(0xFF005D53),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeSnapAppBar(
    title: String,
    onArrowBackIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onArrowBackIconClick) {
                Text("←", style = MaterialTheme.typography.titleLarge)
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ShapeSnapHomeAppBarWithLogoutPreview() {
    ShapeSnapHomeAppBar(
        title = stringResource(Res.string.home_title),
        isLoggedIn = true,
        onMenuClick = {},
        onLoginClick = {},
        onLogoutClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ShapeSnapHomeAppBarWithLoginPreview() {
    ShapeSnapHomeAppBar(
        title = stringResource(Res.string.home_title),
        isLoggedIn = false,
        onMenuClick = {},
        onLoginClick = {},
        onLogoutClick = {},
    )
}