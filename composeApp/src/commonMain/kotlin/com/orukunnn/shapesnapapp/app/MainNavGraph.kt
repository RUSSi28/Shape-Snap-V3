package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orukunnn.shapesnapapp.data.model.user.UserProfile
import com.orukunnn.shapesnapapp.ui.common.AdBannerView
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapBottomBar
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapHomeAppBar
import com.orukunnn.shapesnapapp.ui.common.ShapeSnapRouteAppBar
import org.jetbrains.compose.resources.stringResource
import shapesnapv3.composeapp.generated.resources.Res
import shapesnapv3.composeapp.generated.resources.posts_title
import shapesnapv3.composeapp.generated.resources.settings_title
import shapesnapv3.composeapp.generated.resources.storage_saved_title

fun NavGraphBuilder.mainNavGraph(
    rootNavController: NavHostController,
    onLogoutClick: () -> Unit,
) {
    composable<MainDestination> {
        // 認証切替で Main に来た時点では LocalUserProfile は非 null。
        // graph remember 対策として引数キャプチャではなく Local から読む。
        val userProfile = checkNotNull(LocalUserProfile.current) {
            "MainDestination はログイン済みでのみ表示されます"
        }
        MainShell(
            userProfile = userProfile,
            rootNavController = rootNavController,
            onLogoutClick = onLogoutClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainShell(
    userProfile: UserProfile,
    rootNavController: NavHostController,
    onLogoutClick: () -> Unit,
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when {
                currentDestination.isOnRoute(HomeDestination::class) -> {
                    ShapeSnapHomeAppBar(
                        storedPresets = userProfile.storage.size,
                        titleColor = ShapeSnapColors.Brand,
                        containerColor = ShapeSnapColors.Surface,
                        scrollBehavior = null,
                    )
                }
                currentDestination.isOnRoute(PostsDestination::class) -> {
                    ShapeSnapRouteAppBar(
                        title = stringResource(Res.string.posts_title),
                    )
                }
                currentDestination.isOnRoute(StorageDestination::class) -> {
                    ShapeSnapRouteAppBar(
                        title = stringResource(Res.string.storage_saved_title),
                    )
                }
                currentDestination.isOnRoute(SettingsDestination::class) -> {
                    ShapeSnapRouteAppBar(
                        title = stringResource(Res.string.settings_title),
                    )
                }
            }
        },
        bottomBar = {
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
                            tabNavController.navigate(SearchDestination) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigatePost = {
                        if (!currentDestination.isOnRoute(PostsDestination::class)) {
                            tabNavController.navigate(PostsDestination) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateHome = {
                        if (!currentDestination.isOnRoute(HomeDestination::class)) {
                            tabNavController.navigate(HomeDestination) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateStorage = {
                        if (!currentDestination.isOnRoute(StorageDestination::class)) {
                            tabNavController.navigate(StorageDestination) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateProfile = {
                        if (!currentDestination.isOnRoute(SettingsDestination::class)) {
                            tabNavController.navigate(SettingsDestination) {
                                launchSingleTop = true
                            }
                        }
                    },
                )
                AdBannerView()
            }
        },
    ) { innerPadding ->
        MainTabNavHost(
            userProfile = userProfile,
            tabNavController = tabNavController,
            rootNavController = rootNavController,
            onLogoutClick = onLogoutClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
