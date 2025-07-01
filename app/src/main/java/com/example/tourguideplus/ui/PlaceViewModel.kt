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
    // Получаем DAO и репозиторий
    private val repository: PlaceRepository

    // StateFlow со списком мест
    val places: StateFlow<List<Place>>

    init {
        val dao = AppDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(dao)

        places = repository.allPlaces
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    }

    // Добавить/обновить место
    fun upsert(place: Place) = viewModelScope.launch {
        repository.upsert(place)
    }

    // Удалить место
    fun delete(place: Place) = viewModelScope.launch {
        repository.delete(place)
    }
}