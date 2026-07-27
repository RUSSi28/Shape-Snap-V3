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
fun MainTabNavHost(
    userProfile: UserProfile,
    tabNavController: NavHostController,
    rootNavController: NavHostController,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = tabNavController,
        startDestination = HomeDestination,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
        modifier = modifier,
    ) {
        composable<HomeDestination> {
            AppExitBackHandler()
            HomeScreen(currentUser = userProfile)
        }
        composable<PostsDestination> {
            AppBackHandler(onBack = { tabNavController.navigate(HomeDestination) })
            PostsScreen()
        }
        composable<StorageDestination> {
            AppBackHandler(onBack = { tabNavController.navigate(HomeDestination) })
            StorageScreen()
        }
        composable<SettingsDestination> {
            AppBackHandler(onBack = { tabNavController.navigate(HomeDestination) })
            SettingsScreen(
                address = userProfile.email.orEmpty(),
                onRequestToLogOut = onLogoutClick,
                onRequestToNavigateToTermsOfService = {
                    rootNavController.navigate(TermsOfServiceDestination)
                },
                onRequestToNavigateToContact = {
                    rootNavController.navigate(ContactDestination)
                },
            )
        }
    }
}
