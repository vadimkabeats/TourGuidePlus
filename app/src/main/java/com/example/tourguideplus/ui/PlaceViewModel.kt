package com.example.tourguideplus.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourguideplus.data.database.AppDatabase
import com.example.tourguideplus.data.repository.PlaceRepository
import com.example.tourguideplus.data.model.Place
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlaceRepository

    // Список всех мест
    val places: StateFlow<List<Place>>

    //  Список избранного
    val favoritePlaces: StateFlow<List<Place>>

    init {
        val dao = AppDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(dao)

        places = repository.allPlaces
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

        favoritePlaces = repository.favoritePlaces
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    }

    // добавление/обновление
    fun upsert(place: Place) = viewModelScope.launch {
        repository.upsert(place)
    }

    // удаление
    fun delete(place: Place) = viewModelScope.launch {
        repository.delete(place)
    }
}