package com.orukunnn.shapesnapapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.nav_home
import shapesnapv3.composeapp.generated.resources.nav_posts
import shapesnapv3.composeapp.generated.resources.nav_sign_out
import shapesnapv3.composeapp.generated.resources.nav_storage
import shapesnapv3.composeapp.generated.resources.sheet_storage_count

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    currentUser: UserProfile?,
    onNavigateHome: () -> Unit,
    onNavigatePosts: () -> Unit,
    onNavigateStorage: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!visible) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = currentUser?.displayName.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            NavigationSheetRow(
                onClick = {
                    onNavigateHome()
                    onDismiss()
                },
                label = stringResource(Res.string.nav_home),
            )
            NavigationSheetRow(
                onClick = {
                    onNavigatePosts()
                    onDismiss()
                },
                label = stringResource(Res.string.nav_posts),
            )
            NavigationSheetRow(
                onClick = {
                    onNavigateStorage()
                    onDismiss()
                },
                label = stringResource(Res.string.nav_storage),
            )
            val storageCount = currentUser?.storage?.size ?: 0
            Text(
                text = stringResource(Res.string.sheet_storage_count, storageCount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            NavigationSheetRow(
                onClick = {
                    onSignOut()
                    onDismiss()
                },
                label = stringResource(Res.string.nav_sign_out),
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NavigationSheetRow(
    label: String,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(label) },
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    )
}
