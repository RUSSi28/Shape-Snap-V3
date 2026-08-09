package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.data.model.event.EventLog

interface EventDatasource {
    suspend fun logEvent(event: EventLog): Result<Unit>
}
