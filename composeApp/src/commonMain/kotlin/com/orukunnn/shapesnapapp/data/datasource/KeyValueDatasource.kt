package com.orukunnn.shapesnapapp.data.datasource

interface KeyValueDatasource {
    suspend fun getStringOrNull(key: String): String?

    suspend fun putString(key: String, value: String)

    suspend fun clear()
}
