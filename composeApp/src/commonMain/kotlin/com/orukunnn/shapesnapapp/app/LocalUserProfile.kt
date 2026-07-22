package com.orukunnn.shapesnapapp.app

import androidx.compose.runtime.staticCompositionLocalOf
import com.orukunnn.shapesnapapp.data.model.user.UserProfile

/**
 * Root が購読した [UserProfile] を NavHost 配下へ届ける。
 * graph が remember されても Provider の更新で最新値を読める。
 */
val LocalUserProfile = staticCompositionLocalOf<UserProfile?> { null }
