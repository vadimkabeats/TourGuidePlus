package com.example.tourguideplus.navigation

sealed class Screen(val route: String, val title: String) {
    object Places : Screen("places", "Места")
    object Routes : Screen("routes", "Маршруты")
    object Favorites : Screen("favorites", "Избранное")
    object Weather : Screen("weather", "Погода")
    object Help : Screen("help", "Справка")
    object PlaceForm : Screen("place_form", "Добавить место")
}