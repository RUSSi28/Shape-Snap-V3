package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orukunnn.shapesnapapp.ui.common.AdBannerView
import com.orukunnn.shapesnapapp.ui.common.LogOutConfirmDialog
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapBottomBar
import com.orukunnn.shapesnapapp.ui.login.LoginScreen
import com.orukunnn.shapesnapapp.ui.main.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.KClass

@Composable
fun MainScreen(
    mainViewModel: MainScreenViewModel = koinViewModel(),
) {
    val showLogout by mainViewModel.showLogoutConfirmDialog.collectAsState()
    val userProfile by mainViewModel.userProfile.collectAsState()

    key(userProfile?.uid) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val isBottomBarVisible = userProfile != null && currentDestination != null

        Scaffold(
            bottomBar = {
                if (isBottomBarVisible) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.navigationBars.only(WindowInsetsSides.Bottom),
                        ),
                    ) {
                        ShapeSnapBottomBar(
                            currentDestination = currentDestination,
                            onNavigateSearch = {
                                if (!currentDestination.isOnRoute(SearchDestination::class)) {
                                    navController.navigate(SearchDestination) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onNavigatePost = {
                                if (!currentDestination.isOnRoute(PostsDestination::class)) {
                                    navController.navigate(PostsDestination) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onNavigateHome = {
                                if (!currentDestination.isOnRoute(HomeDestination::class)) {
                                    navController.navigate(HomeDestination) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onNavigateStorage = {
                                if (!currentDestination.isOnRoute(StorageDestination::class)) {
                                    navController.navigate(StorageDestination) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onNavigateProfile = {
                                if (!currentDestination.isOnRoute(SettingsDestination::class)) {
                                    navController.navigate(SettingsDestination) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                        )
                        AdBannerView()
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0),
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                val currentUserProfile = userProfile
                if (currentUserProfile != null) {
                    AppNavHost(
                        userProfile = currentUserProfile,
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        onLogoutClick = { mainViewModel.requestLogoutConfirmation() },
                    )
                } else {
                    LoginScreen()
                }
            }
        }
    }

    if (showLogout) {
        LogOutConfirmDialog(
            onLogOutConfirm = { mainViewModel.confirmLogout() },
            onDismiss = { mainViewModel.dismissLogoutConfirmation() },
        )
    }
}

private fun NavDestination?.isOnRoute(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true
