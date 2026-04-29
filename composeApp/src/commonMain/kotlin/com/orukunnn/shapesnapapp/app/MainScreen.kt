package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orukunnn.shapesnapapp.ui.common.AdBannerView
import com.orukunnn.shapesnapapp.ui.common.LogOutConfirmDialog
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapBottomBar
import com.orukunnn.shapesnapapp.ui.main.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val mainViewModel = koinViewModel<MainScreenViewModel>()
    val user by mainViewModel.sheetUserProfile.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showSheet by remember { mutableStateOf(false) }
    val showLogout by mainViewModel.showLogoutConfirmDialog.collectAsState()
    val sheetUser by mainViewModel.sheetUserProfile.collectAsState()
    val isBottomBarVisible = currentRoute?.let { route ->
        bottomNavRouteNames.any(route::contains)
    } == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            AppNavHost(
                address = user?.email.orEmpty(),
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onMenuClick = { showSheet = true },
                onLogoutClick = { mainViewModel.requestLogoutConfirmation() },
            )
            if (isBottomBarVisible) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
                ) {
                    ShapeSnapBottomBar(
                        currentRoute = currentRoute,
                        onNavigateSearch = {
                            navController.navigate(SearchDestination) { launchSingleTop = true }
                        },
                        onNavigatePost = {
                            navController.navigate(PostsDestination) { launchSingleTop = true }
                        },
                        onNavigateHome = {
                            navController.navigate(HomeDestination) { launchSingleTop = true }
                        },
                        onNavigateStorage = {
                            navController.navigate(StorageDestination) { launchSingleTop = true }
                        },
                        onNavigateProfile = {
                            navController.navigate(ProfileDestination) { launchSingleTop = true }
                        },
                    )
                    AdBannerView(Modifier.padding(bottom = 8.dp))
                }
            }
        }
    }

//    NavigationBottomSheet(
//        visible = showSheet,
//        onDismiss = { showSheet = false },
//        currentUser = sheetUser,
//        onNavigateHome = {
//            navController.navigate(HomeDestination) { launchSingleTop = true }
//        },
//        onNavigatePosts = {
//            navController.navigate(PostsDestination) { launchSingleTop = true }
//        },
//        onNavigateStorage = {
//            navController.navigate(StorageDestination) { launchSingleTop = true }
//        },
//        onSignOut = { mainVm.requestLogoutConfirmation() },
//    )

    if (showLogout) {
        LogOutConfirmDialog(
            onLogOutConfirm = { mainViewModel.confirmLogout() },
            onDismiss = { mainViewModel.dismissLogoutConfirmation() },
        )
    }
}
