package com.example.tourguideplus.data.repository

import com.example.tourguideplus.data.dao.RouteDao
import com.example.tourguideplus.data.model.RouteEntity
import com.example.tourguideplus.data.model.RoutePlaceCrossRef
import com.example.tourguideplus.data.model.RouteWithPlaces
import kotlinx.coroutines.flow.Flow

class RouteRepository(private val dao: RouteDao) {

    /** Поток всех маршрутов с их местами */
    val allRoutesWithPlaces: Flow<List<RouteWithPlaces>> = dao.getAllRoutesWithPlaces()

    suspend fun upsertRoute(route: RouteEntity, placeIds: List<Int>) {
        // вставляем или обновляем маршрут
        val routeId = if (route.id == 0) {
            dao.insertRoute(route).toInt()
        } else {
            dao.insertRoute(route)
            route.id
        }

        // очищаем старые связи
        dao.clearCrossRefs(routeId)
        // создаём новые
        placeIds.forEach { pid ->
            val ref = RoutePlaceCrossRef(routeId = routeId, placeId = pid)
            dao.insertCrossRef(ref)
        }
    }

    /** Удалить маршрут (и его связи) */
    suspend fun deleteRoute(route: RouteEntity) {
        dao.deleteRoute(route)
        // связи автоматически удалены через foreign keys или можно вызвать clearCrossRefs
    }
}