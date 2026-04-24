package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.search_placeholder
import shapesnapv3.composeapp.generated.resources.search_title

@Composable
fun SearchScreen() {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = stringResource(Res.string.search_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(Res.string.search_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
