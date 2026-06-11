package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.ui.home.HomeScreen
import com.orukunnn.shapesnapapp.ui.posts.PostsScreen
import com.orukunnn.shapesnapapp.ui.storage.StorageScreen

@Composable
fun AppNavHost(
    userProfile: UserProfile,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination,
        modifier = modifier,
    ) {
        composable<HomeDestination> {
            HomeScreen(currentUser = userProfile)
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
        composable<SettingsDestination> {
            SettingsScreen(
                address = userProfile.email.orEmpty(),
                onRequestToPopBackStack = { navController.popBackStack() },
                onRequestToLogOut = onLogoutClick,
                onRequestToNavigateToTermsOfService = {
                    navController.navigate(
                        TermsOfServiceDestination
                    )
                },
                onRequestToNavigateToContact = { navController.navigate(ContactDestination) },
            )
        }
        composable<TermsOfServiceDestination> {
            TermsOfServiceScreen(
                onRequestToPopBackStack = { navController.popBackStack() }
            )
        }
        composable<ContactDestination> {
            ContactScreen(
                onRequestToPopBackStack = { navController.popBackStack() }
            )
        }
    }
}
