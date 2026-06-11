package com.orukunnn.shapesnapapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.app.ShapeSnapButton
import com.orukunnn.shapesnapapp.app.ShapeSnapColors
import com.orukunnn.shapesnapapp.ui.common.LoadState
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Outline
import com.woowla.compose.icon.collections.tabler.tabler.outline.BrandGoogle
import com.woowla.compose.icon.collections.tabler.tabler.outline.Mail
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.login_contact
import shapesnapv3.composeapp.generated.resources.login_error
import shapesnapv3.composeapp.generated.resources.login_google

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    when (val state = viewModel.state) {
        is LoadState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Idle, is LoadState.Error, is LoadState.Success -> {
            LoginScreen(
                errorMessage = if (state is LoadState.Error) state.message else "",
                onRequestToLogin = { viewModel.signInWithGoogle() },
                onRequestToContact = {}
            )
        }
    }
}

@Composable
private fun LoginScreen(
    errorMessage: String,
    onRequestToLogin: () -> Unit,
    onRequestToContact: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShapeSnapButton(
            imageVector = Tabler.Outline.BrandGoogle,
            text = stringResource(Res.string.login_google),
            enabled = true,
            color = ShapeSnapColors.Accent,
            onClick = onRequestToLogin,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null,
        )
        ShapeSnapButton(
            imageVector = Tabler.Outline.Mail,
            text = stringResource(Res.string.login_contact),
            enabled = true,
            color = ShapeSnapColors.Brand,
            onClick = onRequestToContact,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null,
        )
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (errorMessage.isNotEmpty()) {
            Column {
                Text(
                    stringResource(Res.string.login_error) + ": $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp),
                )
                Spacer(Modifier.size(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginSuccessScreenPreview() {
    LoginScreen(
        errorMessage = "",
        onRequestToLogin = {},
        onRequestToContact = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginErrorScreenPreview() {
    LoginScreen(
        errorMessage = "ネットワークの通信が悪いか、サーバーとの通信に失敗しました。\n 状態が改善しない場合は開発者に問い合わせてください。",
        onRequestToLogin = {},
        onRequestToContact = {},
    )
}