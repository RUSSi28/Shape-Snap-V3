package com.orukunnn.shapesnapapp.data.repository.preset

import com.orukunnn.shapesnapapp.core.util.AppLogger
import com.orukunnn.shapesnapapp.data.datasource.FirestoreDatasource
import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.coroutines.flow.Flow

class PresetRepositoryImpl(
    private val firestoreDatasource: FirestoreDatasource,
) : PresetRepository {
    override fun observePresets(): Flow<List<Preset>> = firestoreDatasource.observePresets()

    override suspend fun loadPresetsPage(cursor: PresetPageCursor?): Pair<List<Preset>, PresetPageCursor?> {
        return firestoreDatasource.fetchPresetsPage(PresetRepository.PAGE_SIZE, cursor).fold(
            onSuccess = { it },
            onFailure = { e ->
                AppLogger.w("プリセット一覧の取得に失敗", e)
                Pair(emptyList(), null)
            },
        )
    }
}
