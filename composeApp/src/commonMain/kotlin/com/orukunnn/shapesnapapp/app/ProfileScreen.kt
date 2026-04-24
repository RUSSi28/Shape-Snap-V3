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
import shapesnapv3.composeapp.generated.resources.profile_placeholder
import shapesnapv3.composeapp.generated.resources.profile_title

@Composable
fun ProfileScreen() {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = stringResource(Res.string.profile_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(Res.string.profile_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
