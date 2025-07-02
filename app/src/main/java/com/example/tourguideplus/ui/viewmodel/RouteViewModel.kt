package com.example.tourguideplus.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourguideplus.data.database.AppDatabase
import com.example.tourguideplus.data.model.RouteEntity
import com.example.tourguideplus.data.model.RouteWithPlaces
import com.example.tourguideplus.data.repository.RouteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RouteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RouteRepository

    /** Поток всех маршрутов с вложенными местами */
    val routes: StateFlow<List<RouteWithPlaces>>

    init {
        val dao = AppDatabase.getDatabase(application).routeDao()
        repository = RouteRepository(dao)

        routes = repository.allRoutesWithPlaces
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    }

    /** Создать или обновить маршрут с привязкой мест */
    fun upsert(route: RouteEntity, placeIds: List<Int>) = viewModelScope.launch {
        repository.upsertRoute(route, placeIds)
    }

    /** Удалить маршрут */
    fun delete(route: RouteEntity) = viewModelScope.launch {
        repository.deleteRoute(route)
    }
}