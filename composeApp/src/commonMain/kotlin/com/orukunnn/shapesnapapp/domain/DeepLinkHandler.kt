package com.orukunnn.shapesnapapp.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object DeepLinkHandler {
    internal val pendingPresetId: StateFlow<String?>
    field = MutableStateFlow<String?>(null)

    fun receive(url: String) {
        pendingPresetId.value = PresetShareLink.parse(url)
    }

    fun consume(presetId: String) {
        if (pendingPresetId.value == presetId) {
            pendingPresetId.value = null
        }
    }
}
