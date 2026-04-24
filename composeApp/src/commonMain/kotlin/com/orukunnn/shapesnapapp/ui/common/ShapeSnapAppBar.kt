package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.appbar_free_user
import shapesnapv3.composeapp.generated.resources.appbar_login_arrow

@OptIn(ExperimentalMaterial3Api::class)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(Res.string.appbar_free_user),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.appbar_login_arrow),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Text("☰", style = MaterialTheme.typography.titleLarge)
            }
        },
        actions = {
            if (isLoggedIn) {
                LogOutIconButton(onLogoutClick = onLogoutClick)
            } else {
                LogInIconButton(onLoginClick = onLoginClick)
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

@Composable
fun LogInIconButton(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onLoginClick, modifier = modifier) {
        Text("👤", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun LogOutIconButton(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onLogoutClick, modifier = modifier) {
        Text(
            text = "⏻",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF005D53),
        )
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
@Composable
fun ShapeSnapAppBarDrawer(
    title: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Text("☰", style = MaterialTheme.typography.titleLarge)
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
