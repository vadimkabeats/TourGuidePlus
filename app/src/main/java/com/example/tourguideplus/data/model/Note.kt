package com.example.tourguideplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val placeId: Int,       // внешняя связь на Place.id
    val content: String,    // текст заметки
    val timestamp: Long     // когда создана заметка (System.currentTimeMillis())
)