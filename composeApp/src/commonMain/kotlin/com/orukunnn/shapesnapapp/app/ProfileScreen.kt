package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.profile_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    address: String,
    onRequestToPopBackStack: () -> Unit,
    onRequestToLogOut: () -> Unit,
    onRequestToNavigateToTermsOfService: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ShapeSnapAppBar(
                title = stringResource(Res.string.profile_title),
                onArrowBackIconClick = onRequestToPopBackStack,
                scrollBehavior = null
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Box(
                modifier = Modifier.padding(
                    vertical = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(text = "ログイン中のアカウント")
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = address)
                }
            }
            HorizontalDivider()
            Text(
                text = "ログアウト",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onRequestToLogOut() }
                    .padding(8.dp)
            )
            HorizontalDivider()
            Text(
                text = "利用規約",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onRequestToNavigateToTermsOfService() }
                    .padding(8.dp)
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        address = "address@gmail.com",
        onRequestToPopBackStack = {},
        onRequestToLogOut = {},
        onRequestToNavigateToTermsOfService = {}
    )
}
