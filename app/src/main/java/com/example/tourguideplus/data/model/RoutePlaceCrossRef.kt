package com.example.tourguideplus.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "route_place_join",
    primaryKeys = ["routeId", "placeId"],
    indices = [
        Index("routeId"),
        Index("placeId")
    ]
)
data class RoutePlaceCrossRef(
    val routeId: Int,
    val placeId: Int
)