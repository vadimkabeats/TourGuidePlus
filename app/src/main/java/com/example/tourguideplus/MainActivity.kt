package com.example.tourguideplus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tourguideplus.navigation.Screen
import com.example.tourguideplus.ui.*
import com.example.tourguideplus.HelpActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            Scaffold(
                bottomBar = { BottomBar(currentRoute, navController) },
                floatingActionButton = { Fab(currentRoute, navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Places.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    // Места
                    composable(Screen.Places.route) {
                        PlacesScreen(navController)
                    }
                    // Форма места
                    composable(
                        route = Screen.PlaceForm.route,
                        arguments = listOf(navArgument("placeId") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) { backStack ->
                        val raw = backStack.arguments?.getInt("placeId") ?: -1
                        val editId = raw.takeIf { it >= 0 }
                        AddEditPlaceScreen(navController, editId)
                    }
                    // Детали места
                    composable(
                        route = Screen.PlaceDetails.route,
                        arguments = listOf(navArgument("placeId") {
                            type = NavType.IntType
                        })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("placeId") ?: return@composable
                        PlaceDetailsScreen(placeId = id, navController)
                    }

                    // Маршруты
                    composable(Screen.Routes.route) {
                        RoutesScreen(navController)
                    }
                    // Форма маршрута
                    composable(
                        route = Screen.RouteForm.route,
                        arguments = listOf(navArgument("routeId") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) { backStack ->
                        val raw = backStack.arguments?.getInt("routeId") ?: -1
                        val editId = raw.takeIf { it >= 0 }
                        RouteFormScreen(navController, editId)
                    }
                    // Детали маршрута
                    composable(
                        route = Screen.RouteDetails.route,
                        arguments = listOf(navArgument("routeId") {
                            type = NavType.IntType
                        })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("routeId") ?: return@composable
                        RouteDetailsScreen(routeId = id, navController)
                    }

                    // Избранное
                    composable(Screen.Favorites.route) {
                        FavoritesScreen(navController)
                    }
                    // Погода
                    composable(Screen.Weather.route) {
                        WeatherScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(currentRoute: String?, navController: NavHostController) {
    val tabs = listOf(
        Screen.Places, Screen.Routes,
        Screen.Favorites, Screen.Weather,
        Screen.Help
    )
    BottomNavigation {
        tabs.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.Places    -> Icons.Default.Place
                            Screen.Routes    -> Icons.Default.Map
                            Screen.Favorites -> Icons.Default.Favorite
                            Screen.Weather   -> Icons.Default.Cloud
                            Screen.Help      -> Icons.Default.Info
                            else              -> Icons.Default.Place
                        },
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (screen == Screen.Help) {
                        // Открываем HelpActivity
                        navController.context.startActivity(
                            Intent(navController.context, HelpActivity::class.java)
                        )
                    } else {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun Fab(currentRoute: String?, navController: NavHostController) {
    when (currentRoute) {
        Screen.Places.route -> FloatingActionButton(onClick = {
            navController.navigate(Screen.PlaceForm.createRoute(null))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Добавить место")
        }
        Screen.Routes.route -> FloatingActionButton(onClick = {
            navController.navigate(Screen.RouteForm.createRoute(null))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Добавить маршрут")
        }
    }
}
