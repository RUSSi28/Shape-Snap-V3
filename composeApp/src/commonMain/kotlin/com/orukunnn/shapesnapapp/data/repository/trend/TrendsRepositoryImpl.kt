package com.orukunnn.shapesnapapp.data.repository.trend

import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import com.orukunnn.shapesnapapp.data.model.trend.TrendItem
import com.orukunnn.shapesnapapp.data.model.trend.TrendPreset
import com.orukunnn.shapesnapapp.data.model.trend.buildTrendPresets
import com.orukunnn.shapesnapapp.data.model.trend.missingTrendPresetIds
import com.orukunnn.shapesnapapp.data.model.trend.topTrendItems
import com.orukunnn.shapesnapapp.data.repository.preset.PresetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class TrendsRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource,
    presetRepository: PresetRepository,
    scope: CoroutineScope,
) : TrendsRepository {
    override val weeklyTrendPresets: StateFlow<List<TrendPreset>?> =
        combine(
            firestoreDatasource.observeWeeklyTrendItems(),
            presetRepository.presets.filterNotNull(),
        ) { items, presets ->
            items to presets
        }.mapLatest { (items, presets) ->
            resolveTrendPresets(items, presets)
        }.catch { error ->
            AppLogger.e("週間トレンドの購読に失敗", error)
            emit(emptyList())
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private suspend fun resolveTrendPresets(
        items: List<TrendItem>,
        cachedPresets: List<Preset>,
    ): List<TrendPreset> {
        val selected = topTrendItems(items)
        if (selected.isEmpty()) return emptyList()
        val missingIds = missingTrendPresetIds(selected, cachedPresets)
        val fetched =
            if (missingIds.isEmpty()) {
                emptyList()
            } else {
                coroutineScope {
                    missingIds.map { presetId ->
                        async {
                            firestoreDatasource.fetchPresetById(presetId).getOrNull()
                        }
                    }.awaitAll().filterNotNull()
                }
            }
        return buildTrendPresets(selected, cachedPresets + fetched)
    }
}
