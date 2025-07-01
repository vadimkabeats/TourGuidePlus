package com.example.tourguideplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String,
    val imageUri: String?,          // путь к фото
    val isFavorite: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null
)