package com.example.tourguideplus.data.model

import androidx.room.Entity

@Entity(
    tableName = "route_place_join",
    primaryKeys = ["routeId", "placeId"]
)
data class RoutePlaceCrossRef(
    val routeId: Int,
    val placeId: Int
)