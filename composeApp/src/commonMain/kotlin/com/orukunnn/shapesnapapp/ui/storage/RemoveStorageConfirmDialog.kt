package com.orukunnn.shapesnapapp.ui.storage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.dialog_remove_storage_cancel
import shapesnapv3.composeapp.generated.resources.dialog_remove_storage_confirm
import shapesnapv3.composeapp.generated.resources.dialog_remove_storage_error
import shapesnapv3.composeapp.generated.resources.dialog_remove_storage_message
import shapesnapv3.composeapp.generated.resources.dialog_remove_storage_title

@Composable
internal fun RemoveStorageConfirmDialog(
    isRemoving: Boolean,
    removeFailed: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            if (!isRemoving) onDismiss()
        },
        title = {
            Text(stringResource(Res.string.dialog_remove_storage_title))
        },
        text = {
            Column {
                Text(stringResource(Res.string.dialog_remove_storage_message))
                AnimatedVisibility(
                    visible = removeFailed,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                ) {
                    Text(
                        text = stringResource(Res.string.dialog_remove_storage_error),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isRemoving,
            ) {
                if (isRemoving) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(stringResource(Res.string.dialog_remove_storage_confirm))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isRemoving,
            ) {
                Text(stringResource(Res.string.dialog_remove_storage_cancel))
            }
        },
    )
}
