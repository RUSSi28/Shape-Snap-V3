package com.orukunnn.shapesnapapp.app

import kotlinx.serialization.Serializable

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
