package com.orukunnn.shapesnapapp.app

import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

interface BottomNavDestination

@Serializable
data object HomeDestination : BottomNavDestination

@Serializable
data object PostsDestination : BottomNavDestination

@Serializable
data object StorageDestination : BottomNavDestination

@Serializable
data object SearchDestination : BottomNavDestination

@Serializable
data object SettingsDestination : BottomNavDestination

val BottomNavDestinationRoutes: List<KClass<out BottomNavDestination>> = listOf(
    HomeDestination::class,
    PostsDestination::class,
    StorageDestination::class,
    SearchDestination::class,
    SettingsDestination::class,
)
