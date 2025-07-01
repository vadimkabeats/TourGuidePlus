package com.example.tourguideplus.data.dao

import androidx.room.*
import com.example.tourguideplus.data.model.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places")
    fun getAll(): Flow<List<Place>>

    @Query("SELECT * FROM places WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Place>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Update
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)
}