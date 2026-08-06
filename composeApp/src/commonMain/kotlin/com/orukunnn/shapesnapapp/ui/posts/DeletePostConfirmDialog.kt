package com.orukunnn.shapesnapapp.ui.posts

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
import shapesnapv3.composeapp.generated.resources.dialog_delete_post_cancel
import shapesnapv3.composeapp.generated.resources.dialog_delete_post_confirm
import shapesnapv3.composeapp.generated.resources.dialog_delete_post_error
import shapesnapv3.composeapp.generated.resources.dialog_delete_post_message
import shapesnapv3.composeapp.generated.resources.dialog_delete_post_title

@Composable
internal fun DeletePostConfirmDialog(
    isDeleting: Boolean,
    deleteFailed: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            if (!isDeleting) onDismiss()
        },
        title = {
            Text(stringResource(Res.string.dialog_delete_post_title))
        },
        text = {
            Column {
                Text(stringResource(Res.string.dialog_delete_post_message))
                AnimatedVisibility(
                    visible = deleteFailed,
                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                ) {
                    Text(
                        text = stringResource(Res.string.dialog_delete_post_error),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isDeleting,
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(stringResource(Res.string.dialog_delete_post_confirm))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting,
            ) {
                Text(stringResource(Res.string.dialog_delete_post_cancel))
            }
        },
    )
}
