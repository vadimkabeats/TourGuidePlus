package com.example.tourguideplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_logs")
data class VisitLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val placeId: Int,       // связь на Place.id
    val visitedAt: Long     // отметка времени визита
)