package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapAppBar
import com.orukunnn.shapesnapapp.ui.preset.PresetDetailScreen

fun NavGraphBuilder.presetDetailNavGraph(
    rootNavController: NavHostController,
) {
    composable<PresetDetailDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<PresetDetailDestination>()
        val userProfile = checkNotNull(LocalUserProfile.current) {
            "PresetDetailDestination はログイン済みでのみ表示されます"
        }
        PresetDetailShell(
            onBack = rootNavController::navigateToHome,
        ) {
            PresetDetailScreen(
                presetId = destination.presetId,
                currentUser = userProfile,
                onBack = rootNavController::navigateToHome,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetDetailShell(
    onBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ShapeSnapAppBar(
                title = "プリセット詳細",
                onArrowBackIconClick = onBack,
            )
        },
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            content()
        }
    }
}
