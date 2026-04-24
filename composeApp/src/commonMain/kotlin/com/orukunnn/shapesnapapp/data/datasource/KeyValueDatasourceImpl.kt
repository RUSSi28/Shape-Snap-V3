package com.orukunnn.shapesnapapp.data.datasource

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toSuspendSettings

@OptIn(ExperimentalSettingsApi::class)
class KeyValueDatasourceImpl(
    private val settings: Settings,
) : KeyValueDatasource {
    private val suspendSettings = settings.toSuspendSettings()

    override suspend fun getStringOrNull(key: String): String? = suspendSettings.getStringOrNull(key)

    override suspend fun putString(key: String, value: String) {
        suspendSettings.putString(key, value)
    }

    override suspend fun clear() {
        settings.clear()
    }
}
