package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.dialog_limit_dismiss
import shapesnapv3.composeapp.generated.resources.dialog_limit_message
import shapesnapv3.composeapp.generated.resources.dialog_limit_title
import shapesnapv3.composeapp.generated.resources.dialog_exit_cancel
import shapesnapv3.composeapp.generated.resources.dialog_exit_confirm
import shapesnapv3.composeapp.generated.resources.dialog_exit_message
import shapesnapv3.composeapp.generated.resources.dialog_exit_title
import shapesnapv3.composeapp.generated.resources.dialog_logout_cancel
import shapesnapv3.composeapp.generated.resources.dialog_logout_confirm
import shapesnapv3.composeapp.generated.resources.dialog_logout_message
import shapesnapv3.composeapp.generated.resources.dialog_logout_title

@Composable
fun LimitReachedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.dialog_limit_title)) },
        text = { Text(stringResource(Res.string.dialog_limit_message)) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.dialog_limit_dismiss))
            }
        },
    )
}

@Composable
fun LogOutConfirmDialog(
    onLogOutConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.dialog_logout_title)) },
        text = { Text(stringResource(Res.string.dialog_logout_message)) },
        confirmButton = {
            TextButton(onClick = onLogOutConfirm) {
                Text(stringResource(Res.string.dialog_logout_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.dialog_logout_cancel))
            }
        },
    )
}

@Composable
fun AppExitConfirmDialog(
    onExitConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.dialog_exit_title)) },
        text = { Text(stringResource(Res.string.dialog_exit_message)) },
        confirmButton = {
            TextButton(onClick = onExitConfirm) {
                Text(stringResource(Res.string.dialog_exit_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.dialog_exit_cancel))
            }
        },
    )
}
