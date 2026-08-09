package com.orukunnn.shapesnapapp.data.datasource

import com.orukunnn.shapesnapapp.data.model.event.EventLog
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException

class EventDatasourceImpl : EventDatasource {
    private val firestore get() = Firebase.firestore(Firebase.app, DATABASE_ID)

    override suspend fun logEvent(event: EventLog): Result<Unit> =
        try {
            firestore.collection(COL_USER_EVENTS).add(EventLog.serializer(), event)
            Result.success(Unit)
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
            Result.failure(error)
        }

    private companion object {
        const val DATABASE_ID = "shape-snap"
        const val COL_USER_EVENTS = "userEvents"
    }
}
