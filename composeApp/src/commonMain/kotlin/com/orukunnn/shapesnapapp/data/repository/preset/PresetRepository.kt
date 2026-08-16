package com.orukunnn.shapesnapapp.data.repository.preset

import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PresetRepository {
    /** 未取得時は null。一度でも Firestore から受信したら非 null（空リスト含む）。 */
    val presets: StateFlow<List<Preset>?>

    fun observePresets(): Flow<List<Preset>>

    suspend fun fetchPresetById(presetId: String): Result<Preset?>

    suspend fun loadPresetsPage(cursor: PresetPageCursor? = null): Result<Pair<List<Preset>, PresetPageCursor?>>

    companion object {
        const val PAGE_SIZE: Int = 10
    }
}
