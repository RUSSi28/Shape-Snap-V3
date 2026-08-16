package com.orukunnn.shapesnapapp.data.repository.trend

import com.orukunnn.shapesnapapp.data.model.trend.TrendPreset
import kotlinx.coroutines.flow.StateFlow

interface TrendsRepository {
    /** 未取得時は null。一度でも解決したら非 null（空リスト含む）。 */
    val weeklyTrendPresets: StateFlow<List<TrendPreset>?>
}
