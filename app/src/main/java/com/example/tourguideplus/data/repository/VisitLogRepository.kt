package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.dao.VisitLogDao
import com.example.tourguideplus.data.model.VisitLog
import kotlinx.coroutines.flow.Flow

class VisitLogRepository(private val dao: VisitLogDao) {
    /** Журнал посещений для заданного места */
    fun getLogsForPlace(placeId: Int): Flow<List<VisitLog>> =
        dao.getLogsForPlace(placeId)

    /** Записать факт посещения */
    suspend fun logVisit(log: VisitLog) {
        dao.insert(log)
    }
}