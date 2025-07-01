package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.dao.PlaceDao
import com.example.tourguideplus.data.model.Place
import kotlinx.coroutines.flow.Flow

class PlaceRepository(
    private val placeDao: PlaceDao
) {
    // Поток всех мест
    val allPlaces: Flow<List<Place>> = placeDao.getAll()

    // Поток избранных мест
    val favoritePlaces: Flow<List<Place>> = placeDao.getFavorites()

    // Добавить или обновить место
    suspend fun upsert(place: Place) {
        placeDao.insert(place)
    }

    // Удалить
    suspend fun delete(place: Place) {
        placeDao.delete(place)
    }
}