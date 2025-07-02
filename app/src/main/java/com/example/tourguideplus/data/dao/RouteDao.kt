package com.example.tourguideplus.data.dao

import androidx.room.*
import com.example.tourguideplus.data.model.RouteEntity
import com.example.tourguideplus.data.model.RoutePlaceCrossRef
import com.example.tourguideplus.data.model.RouteWithPlaces
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    /** Получить все маршруты вместе со списком мест */
    @Transaction
    @Query("SELECT * FROM routes")
    fun getAllRoutesWithPlaces(): Flow<List<RouteWithPlaces>>

    /** Вставить или обновить маршрут; возвращает id (если вставка) */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteEntity): Long

    /** Вставить новую связь маршрут–место, но не дублировать */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(ref: RoutePlaceCrossRef)

    /** Удалить маршрут */
    @Delete
    suspend fun deleteRoute(route: RouteEntity)

    /** Удалить все связи для данного маршрута */
    @Query("DELETE FROM route_place_join WHERE routeId = :routeId")
    suspend fun clearCrossRefs(routeId: Int)
}