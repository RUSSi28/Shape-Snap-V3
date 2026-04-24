package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.ui.common.LoadState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.login_error
import shapesnapv3.composeapp.generated.resources.login_google
import shapesnapv3.composeapp.generated.resources.login_title

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.login_title), style = MaterialTheme.typography.headlineSmall)
        when (val s = state) {
            LoadState.Loading -> CircularProgressIndicator(Modifier.padding(top = 16.dp))
            is LoadState.Error ->
                Text(
                    stringResource(Res.string.login_error) + ": ${s.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp),
                )
            else -> {}
        }
        Button(
            onClick = { viewModel.signInWithGoogle() },
            modifier = Modifier.padding(top = 24.dp),
            enabled = state !is LoadState.Loading,
        ) {
            Text(stringResource(Res.string.login_google))
        }
    }
}
