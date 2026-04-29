package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.search_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRequestToPopBackStack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ShapeSnapAppBar(
                title = stringResource(Res.string.search_title),
                onArrowBackIconClick = onRequestToPopBackStack,
                scrollBehavior = null
            )
        },
    ) { innerPadding ->

    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen(
        onRequestToPopBackStack = {}
    )
}
