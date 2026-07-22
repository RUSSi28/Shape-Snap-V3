package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.settings_contact
import shapesnapv3.composeapp.generated.resources.settings_logged_in_account
import shapesnapv3.composeapp.generated.resources.settings_logout
import shapesnapv3.composeapp.generated.resources.settings_terms_of_service

@Composable
fun SettingsScreen(
    address: String,
    onRequestToLogOut: () -> Unit,
    onRequestToNavigateToTermsOfService: () -> Unit,
    onRequestToNavigateToContact: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            ) {
                Text(text = stringResource(Res.string.settings_logged_in_account))
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = address)
            }
        }
        HorizontalDivider()
        Text(
            text = stringResource(Res.string.settings_logout),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRequestToLogOut() }
                .padding(8.dp),
        )
        HorizontalDivider()
        Text(
            text = stringResource(Res.string.settings_terms_of_service),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRequestToNavigateToTermsOfService() }
                .padding(8.dp),
        )
        HorizontalDivider()
        Text(
            text = stringResource(Res.string.settings_contact),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRequestToNavigateToContact() }
                .padding(8.dp),
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        address = "address@gmail.com",
        onRequestToLogOut = {},
        onRequestToNavigateToTermsOfService = {},
        onRequestToNavigateToContact = {},
    )
}
