package com.orukunnn.shapesnapapp.app

import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data object MainDestination

@Serializable
data class PresetDetailDestination(
    val presetId: String,
)

@Serializable
data object TermsOfServiceDestination

@Serializable
data object ContactDestination
