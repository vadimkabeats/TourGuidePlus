package com.example.tourguideplus.data.dao

import androidx.room.*
import com.example.tourguideplus.data.model.VisitLog
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitLogDao {
    @Query("SELECT * FROM visit_logs WHERE placeId = :placeId ORDER BY visitedAt DESC")
    fun getLogsForPlace(placeId: Int): Flow<List<VisitLog>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(log: VisitLog)
}