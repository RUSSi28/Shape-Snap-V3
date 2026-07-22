package com.orukunnn.shapesnapapp.app

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlin.reflect.KClass

fun NavDestination?.isOnRoute(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true
