package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.appbar_app_name
import shapesnapv3.composeapp.generated.resources.appbar_storage_format

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShapeSnapHomeAppBar(
    storedPresets: Int,
    titleColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.appbar_app_name),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(Res.string.appbar_storage_format, storedPresets),
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.size(16.dp))
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                titleContentColor = titleColor,
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
                tint = ShapeSnapColors.IconTint,
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
@Composable
fun ShapeSnapRouteAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
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
        storedPresets = 3,
        titleColor = ShapeSnapColors.Brand,
        containerColor = ShapeSnapColors.Surface,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ShapeSnapHomeAppBarWithLoginPreview() {
    ShapeSnapHomeAppBar(
        storedPresets = 3,
        titleColor = ShapeSnapColors.Surface,
        containerColor = ShapeSnapColors.Brand,
    )
}