package com.orukunnn.shapesnapapp.data.repository.preset

import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class PresetRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource,
    scope: CoroutineScope,
) : PresetRepository {
    override val presets: StateFlow<List<Preset>?> =
        firestoreDatasource
            .observePresets()
            .catch { e ->
                AppLogger.e("プリセット一覧の購読に失敗", e)
                emit(emptyList())
            }.stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )

    override fun observePresets(): Flow<List<Preset>> = presets.filterNotNull()

    override suspend fun fetchPresetById(presetId: String): Result<Preset?> {
        val cachedPreset = presets.value?.firstOrNull { it.id == presetId }
        return if (cachedPreset != null) {
            Result.success(cachedPreset)
        } else {
            firestoreDatasource.fetchPresetById(presetId)
        }
    }

    override suspend fun loadPresetsPage(
        cursor: PresetPageCursor?,
    ): Result<Pair<List<Preset>, PresetPageCursor?>> =
        firestoreDatasource.fetchPresetsPage(PresetRepository.PAGE_SIZE, cursor)
            .onFailure { e ->
                AppLogger.w("プリセット一覧の取得に失敗", e)
            }
}
