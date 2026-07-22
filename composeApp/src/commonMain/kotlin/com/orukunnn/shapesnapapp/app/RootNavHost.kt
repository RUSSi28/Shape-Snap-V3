package com.orukunnn.shapesnapapp.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.orukunnn.shapesnapapp.ui.main.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RootNavHost(
    mainViewModel: MainScreenViewModel = koinViewModel(),
) {
    val userProfile by mainViewModel.userProfile.collectAsState()
    val isLoggedIn = userProfile != null
    val rootNavController = rememberNavController()

    LaunchedEffect(isLoggedIn) {
        val current = rootNavController.currentBackStackEntry?.destination
        val onLogin = current?.hasRoute<LoginDestination>() == true

        if (isLoggedIn) {
            if (onLogin || current == null) {
                rootNavController.navigate(MainDestination) {
                    popUpTo(LoginDestination) { inclusive = true }
                    launchSingleTop = true
                }
            }
        } else if (!onLogin) {
            rootNavController.navigate(LoginDestination) {
                popUpTo(rootNavController.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

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
            userProfile = userProfile,
            mainViewModel = mainViewModel,
        )
        subNavGraph(rootNavController = rootNavController)
    }
}
