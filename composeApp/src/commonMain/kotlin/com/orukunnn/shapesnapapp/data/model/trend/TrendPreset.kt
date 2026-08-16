package com.orukunnn.shapesnapapp.data.model.trend

import com.orukunnn.shapesnapapp.data.model.preset.Preset

data class TrendPreset(
    val preset: Preset,
    val rank: Int,
    val score: Double,
)

fun topTrendItems(
    items: List<TrendItem>,
    maxItems: Int = MAX_TREND_ITEMS,
): List<TrendItem> =
    items
        .sortedWith(compareByDescending { it.score })
        .distinctBy { it.presetId }
        .take(maxItems)

fun missingTrendPresetIds(
    items: List<TrendItem>,
    presets: List<Preset>,
    maxItems: Int = MAX_TREND_ITEMS,
): List<String> {
    val cachedIds = presets.map { it.id }.toSet()
    return topTrendItems(items, maxItems).map { it.presetId }.filter { it !in cachedIds }
}

fun buildTrendPresets(
    items: List<TrendItem>,
    presets: List<Preset>,
    maxItems: Int = MAX_TREND_ITEMS,
): List<TrendPreset> {
    val presetsById = presets.associateBy { it.id }
    return topTrendItems(items, maxItems).mapNotNull { item ->
        presetsById[item.presetId]?.let { preset ->
            TrendPreset(
                preset = preset,
                rank = item.rank,
                score = item.score,
            )
        }
    }.mapIndexed { index, trendPreset ->
        trendPreset.copy(rank = index + 1)
    }
}

const val MAX_TREND_ITEMS: Int = 10
