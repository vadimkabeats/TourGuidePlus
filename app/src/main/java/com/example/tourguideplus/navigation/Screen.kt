package com.example.tourguideplus.navigation

sealed class Screen(val route: String, val title: String) {
    object Places : Screen("places", "Места")
    object Routes : Screen("routes", "Маршруты")
    object Favorites : Screen("favorites", "Избранное")
    object Weather : Screen("weather", "Погода")
    object Help : Screen("help", "Справка")

    // Экран создания/редактирования маршрута
    object RouteForm : Screen("route_form/{routeId}", "Маршрут") {
        // По умолчанию —1 означает «новый»
        fun createRoute(routeId: Int? = null) = "route_form/${routeId ?: -1}"
    }
    // Экран деталей маршрута
    object RouteDetails   : Screen("route_details/{routeId}", "Детали маршрута") {
        fun createRoute(routeId: Int): String = "route_details/$routeId"
    }
    object PlaceForm : Screen("place_form/{placeId}", "Добавить/Редактировать") {
        // по умолчанию -1 = новый
        fun createRoute(placeId: Int? = null) = "place_form/${placeId ?: -1}"
    }
    object PlaceDetails : Screen("place_details/{placeId}", "Детали") {
        fun createRoute(placeId: Int) = "place_details/$placeId"
    }
}