package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.posts.PostsScreen
import com.orukunnn.shapesnapapp.ui.storage.StorageScreen

@Composable
fun AppNavHost(
    address: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination,
        modifier = modifier,
    ) {
        composable<HomeDestination> {
            HomeScreen(
                onMenuClick = onMenuClick,
                onLogoutClick = onLogoutClick,
            )
        }
        composable<PostsDestination> {
            PostsScreen(onBack = { navController.popBackStack() })
        }
        composable<StorageDestination> {
            StorageScreen(onBack = { navController.popBackStack() })
        }
        composable<SearchDestination> {
            SearchScreen()
        }
        composable<ProfileDestination> {
            ProfileScreen(
                address = address,
                onRequestToPopBackStack = { navController.popBackStack() },
                onRequestToLogOut = onLogoutClick,
            )
        }
    }
}
