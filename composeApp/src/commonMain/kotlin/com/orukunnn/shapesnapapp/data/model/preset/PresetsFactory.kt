package com.orukunnn.shapesnapapp.data.model.preset

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Instant

object PresetsFactory {
    fun createPresets(): ImmutableList<Preset> =
        listOf(
            Preset(
                id = "sample_circle",
                characterTagId = "Circle",
                description = "サンプルプリセット",
                createdAt = Instant.fromEpochSeconds(1_700_000_000, 0),
            ),
            Preset(
                id = "sample_square",
                characterTagId = "Square",
                description = "サンプルプリセット",
                createdAt = Instant.fromEpochSeconds(1_700_000_100, 0),
            ),
        ).toPersistentList()

    fun createPreset(): Preset = Preset(
        id = "sample_square",
        characterTagId = "Square",
        description = "サンプルプリセット",
        createdAt = Instant.fromEpochSeconds(1_700_000_100, 0),
    )
}
