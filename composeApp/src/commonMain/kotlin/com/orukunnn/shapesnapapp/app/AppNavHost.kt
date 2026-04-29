package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
            StorageScreen(
                onRequestToPopBackStack = { navController.popBackStack() },
            )
        }
        composable<SearchDestination> {
            SearchScreen(
                onRequestToPopBackStack = { navController.popBackStack() }
            )
        }
        composable<ProfileDestination> {
            ProfileScreen(
                address = address,
                onRequestToPopBackStack = { navController.popBackStack() },
                onRequestToLogOut = onLogoutClick,
                onRequestToNavigateToTermsOfService = { navController.navigate(TermsOfServiceDestination) }
            )
        }
        composable<TermsOfServiceDestination> {
            TermsOfServiceScreen(
                onRequestToPopBackStack = { navController.popBackStack() }
            )
        }
    }
}
