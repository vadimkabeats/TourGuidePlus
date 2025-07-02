package com.example.tourguideplus.data.dao

import androidx.room.*
import com.example.tourguideplus.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE placeId = :placeId ORDER BY timestamp DESC")
    fun getNotesForPlace(placeId: Int): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)
}