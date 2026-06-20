package com.orukunnn.shapesnapapp.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
        modifier = modifier,
    ) {
        composable<HomeDestination> {
            AppBackHandler(onBack = rememberAppExit())
            HomeScreen(currentUser = userProfile)
        }
        composable<PostsDestination> {
            AppBackHandler(onBack = { navController.navigate(HomeDestination) })
            PostsScreen()
        }
        composable<StorageDestination> {
            AppBackHandler(onBack = { navController.navigate(HomeDestination) })
            StorageScreen()
        }
        composable<SettingsDestination> {
            AppBackHandler(onBack = { navController.navigate(HomeDestination) })
            SettingsScreen(
                address = userProfile.email.orEmpty(),
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