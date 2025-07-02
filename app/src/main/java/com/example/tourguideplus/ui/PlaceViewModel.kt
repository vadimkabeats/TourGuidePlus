package com.example.tourguideplus.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourguideplus.data.database.AppDatabase
import com.example.tourguideplus.data.model.Place
import com.example.tourguideplus.data.repository.PlaceRepository
import com.example.tourguideplus.data.repository.WikiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlaceRepository
    private val wikiRepo: WikiRepository

    /** Все места */
    val places: StateFlow<List<Place>>
    /** Только избранные */
    val favoritePlaces: StateFlow<List<Place>>
    /** Текст из Wikipedia */
    private val _wikiExtract = MutableStateFlow<String?>(null)
    val wikiExtract: StateFlow<String?> = _wikiExtract

    init {
        val dao = AppDatabase.getDatabase(application).placeDao()
        repository = PlaceRepository(dao)
        wikiRepo    = WikiRepository()

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

    fun loadWiki(placeName: String) = viewModelScope.launch {
        _wikiExtract.value = null
        val result = wikiRepo.fetchExtract(placeName)
        if (result.isSuccess) {
            val text = result.getOrNull().orEmpty()
            _wikiExtract.value = text.takeIf { it.isNotBlank() }
                ?: "Информация не найдена"
        } else {
            _wikiExtract.value = "Информация не найдена"
        }
    }

    /** Добавить или обновить место */
    fun upsert(place: Place) = viewModelScope.launch {
        repository.upsert(place)
    }

    /** Удалить место */
    fun delete(place: Place) = viewModelScope.launch {
        repository.delete(place)
    }
}
