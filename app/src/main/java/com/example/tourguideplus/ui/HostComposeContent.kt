package com.example.tourguideplus.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tourguideplus.navigation.Screen
import com.example.tourguideplus.ui.viewmodel.PlaceViewModel

@Composable
fun HostComposeContent(startRoute: String) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.statusBarsPadding()
    ) { inner ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(inner)
        ) {
            composable(Screen.Places.route)    { PlacesScreen(navController) }
            composable(Screen.Routes.route)    { RoutesScreen(navController) }
            composable(Screen.Favorites.route) { FavoritesScreen(navController) }
            composable(Screen.Weather.route)   { WeatherScreen(navController) }
            composable(Screen.PlaceForm.route,
                arguments = listOf(navArgument("placeId") {
                    type = NavType.IntType; defaultValue = -1
                })
            ) { back ->
                val id = back.arguments?.getInt("placeId")!!.takeIf { it >= 0 }
                AddEditPlaceScreen(navController, id)
            }
            composable(Screen.RouteForm.route,
                arguments = listOf(navArgument("routeId") {
                    type = NavType.IntType; defaultValue = -1
                })
            ) { back ->
                val id = back.arguments?.getInt("routeId")!!.takeIf { it >= 0 }
                RouteFormScreen(navController, id)
            }
            composable(Screen.PlaceDetails.route,
                arguments = listOf(navArgument("placeId"){ type=NavType.IntType })
            ) { back ->
                val id = back.arguments?.getInt("placeId")!!
                PlaceDetailsScreen(placeId = id, navController)
            }
            composable(Screen.RouteDetails.route,
                arguments = listOf(navArgument("routeId"){ type=NavType.IntType })
            ) { back ->
                val id = back.arguments?.getInt("routeId")!!
                RouteDetailsScreen(routeId = id, navController)
            }
        }
    }
}
