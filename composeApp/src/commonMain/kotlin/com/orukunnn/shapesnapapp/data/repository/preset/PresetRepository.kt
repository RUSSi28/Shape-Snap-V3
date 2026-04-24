package com.orukunnn.shapesnapapp.data.repository.preset

import com.orukunnn.shapesnapapp.data.model.preset.Preset
import kotlinx.coroutines.flow.Flow

interface PresetRepository {
    fun observePresets(): Flow<List<Preset>>

    suspend fun loadPresetsPage(cursor: PresetPageCursor? = null): Pair<List<Preset>, PresetPageCursor?>

    companion object {
        const val PAGE_SIZE: Int = 4
    }
}
