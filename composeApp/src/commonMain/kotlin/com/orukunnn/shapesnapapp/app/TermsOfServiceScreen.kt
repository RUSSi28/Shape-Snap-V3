package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onRequestToPopBackStack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ShapeSnapAppBar(
                title = "利用規約",
                onArrowBackIconClick = onRequestToPopBackStack,
                scrollBehavior = null,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text("利用規約の内容をここに表示します。")
        }
    }
}

@Preview
@Composable
private fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen(
        onRequestToPopBackStack = {},
    )
}
