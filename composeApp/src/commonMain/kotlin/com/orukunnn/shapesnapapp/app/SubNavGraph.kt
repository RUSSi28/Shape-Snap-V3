package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.contact_title
import shapesnapv3.composeapp.generated.resources.terms_of_service_title

fun NavGraphBuilder.subNavGraph(
    rootNavController: NavHostController,
) {
    composable<TermsOfServiceDestination> {
        SubScreenShell(
            title = stringResource(Res.string.terms_of_service_title),
            onBack = { rootNavController.popBackStack() },
        ) {
            TermsOfServiceContent()
        }
    }
    composable<ContactDestination> {
        SubScreenShell(
            title = stringResource(Res.string.contact_title),
            onBack = { rootNavController.popBackStack() },
        ) {
            ContactContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubScreenShell(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    AppBackHandler(onBack = onBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ShapeSnapAppBar(
                title = title,
                onArrowBackIconClick = onBack,
                scrollBehavior = null,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            content()
        }
    }
}

@Composable
fun TermsOfServiceContent() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = TermsOfService.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp,
        )
    }
}

@Composable
fun ContactContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text("お問い合わせはこちら。")
    }
}

@Preview
@Composable
private fun TermsOfServiceContentPreview() {
    TermsOfServiceContent()
}

@Preview
@Composable
private fun ContactContentPreview() {
    ContactContent()
}
