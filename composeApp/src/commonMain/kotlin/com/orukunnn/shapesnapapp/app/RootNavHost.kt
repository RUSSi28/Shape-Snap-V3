package com.orukunnn.shapesnapapp.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.orukunnn.shapesnapapp.domain.DeepLinkHandler
import com.orukunnn.shapesnapapp.ui.common.LogOutConfirmDialog
import com.orukunnn.shapesnapapp.ui.main.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 単一のルート NavHost（Login / Main / Sub）。
 * 認証切替は [LaunchedEffect] による navigate + popUpTo で行い、スタックに相手側を残さない。
 */
@Composable
fun RootNavHost(
    mainViewModel: MainScreenViewModel = koinViewModel(),
) {
    val userProfile by mainViewModel.userProfile.collectAsState()
    val showLogout by mainViewModel.showLogoutConfirmDialog.collectAsState()
    val pendingPresetId by DeepLinkHandler.pendingPresetId.collectAsState()
    val rootNavController = rememberNavController()
    val isLoggedIn = userProfile != null

    LaunchedEffect(isLoggedIn, pendingPresetId) {
        val current = rootNavController.currentBackStackEntry?.destination
        val pendingId = pendingPresetId
        val onLogin = current?.hasRoute<LoginDestination>() == true
        val onMain = current.isOnRoute(MainDestination::class)
        val onPresetDetail = current.isOnRoute(PresetDetailDestination::class)
        val onSub = current.isOnRoute(TermsOfServiceDestination::class) ||
            current.isOnRoute(ContactDestination::class)

        when {
            isLoggedIn && pendingId != null -> {
                rootNavController.navigateToHome()
                rootNavController.navigate(PresetDetailDestination(pendingId)) {
                    launchSingleTop = true
                }
                DeepLinkHandler.consume(pendingId)
            }
            isLoggedIn && !onMain && !onPresetDetail && !onSub -> {
                rootNavController.navigate(MainDestination) {
                    popUpTo(LoginDestination) { inclusive = true }
                    launchSingleTop = true
                }
            }
            !isLoggedIn && !onLogin && current != null -> {
                rootNavController.navigate(LoginDestination) {
                    popUpTo(rootNavController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    CompositionLocalProvider(LocalUserProfile provides userProfile) {
        NavHost(
            navController = rootNavController,
            startDestination = LoginDestination,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() },
            popExitTransition = { fadeOut() },
        ) {
            loginNavGraph()
            mainNavGraph(
                rootNavController = rootNavController,
                onLogoutClick = mainViewModel::requestLogoutConfirmation,
            )
            presetDetailNavGraph(rootNavController = rootNavController)
            subNavGraph(rootNavController = rootNavController)
        }
    }

    if (showLogout) {
        LogOutConfirmDialog(
            onLogOutConfirm = mainViewModel::confirmLogout,
            onDismiss = mainViewModel::dismissLogoutConfirmation,
        )
    }
}

fun NavHostController.navigateToHome() {
    navigate(MainDestination) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}
