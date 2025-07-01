package com.example.tourguideplus.navigation

sealed class Screen(val route: String, val title: String) {
    object Places : Screen("places", "Места")
    object Routes : Screen("routes", "Маршруты")
    object Favorites : Screen("favorites", "Избранное")
    object Weather : Screen("weather", "Погода")
    object Help : Screen("help", "Справка")

    // Экран формы: без id — добавление, с id — редактирование
    object PlaceForm : Screen("place_form/{placeId}", "Добавить/Редактировать") {
        // Если id == null, навигируем просто на "place_form/null" — потом сможем ловить и понимать, что это добавление
        fun createRoute(placeId: Int? = null): String =
            "place_form/${placeId ?: "null"}"
    }

    // Экран деталей места с обязательным параметром placeId
    object PlaceDetails : Screen("place_details/{placeId}", "Детали") {
        fun createRoute(placeId: Int): String =
            "place_details/$placeId"
    }
}