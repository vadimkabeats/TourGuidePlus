package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.dao.NoteDao
import com.example.tourguideplus.data.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {
    /** Все заметки для заданного места, в порядке убывания времени */
    fun getNotesForPlace(placeId: Int): Flow<List<Note>> =
        dao.getNotesForPlace(placeId)

    /** Добавить или обновить заметку */
    suspend fun upsert(note: Note) {
        dao.insert(note)
    }

    /** Удалить заметку */
    suspend fun delete(note: Note) {
        dao.delete(note)
    }
}