package com.example.tourguideplus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            val backStack by navController.currentBackStackEntryAsState()
            val currentRoute = backStack?.destination?.route

            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val tabs = listOf(
                            Screen.Places,
                            Screen.Routes,
                            Screen.Favorites,
                            Screen.Weather,
                            Screen.Help
                        )
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
                                        startActivity(Intent(this@MainActivity, HelpActivity::class.java))
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
                },
                floatingActionButton = {
                    if (currentRoute == Screen.Places.route) {
                        FloatingActionButton(onClick = {
                            navController.navigate(Screen.PlaceForm.createRoute(null))
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Добавить место")
                        }
                    }
                }
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
                    composable(
                        route = Screen.PlaceForm.route,
                        arguments = listOf(navArgument("placeId") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) { backStack ->
                        // получаем int (-1 означает «новое»)
                        val raw = backStack.arguments?.getInt("placeId") ?: -1
                        val editId = raw.takeIf { it >= 0 }  // null, если -1
                        AddEditPlaceScreen(navController = navController, editPlaceId = editId)
                    }
                    composable(
                        route = Screen.PlaceDetails.route,
                        arguments = listOf(navArgument("placeId") {
                            type = NavType.IntType
                        })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("placeId") ?: return@composable
                        PlaceDetailsScreen(id, navController)
                    }

                    // Маршруты
                    composable(Screen.Routes.route) {
                        RoutesScreen(navController)
                    }
                    composable(
                        route = Screen.RouteForm.route,
                        arguments = listOf(navArgument("routeId") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) { backStack ->
                        val rawId = backStack.arguments?.getInt("routeId") ?: -1
                        // -1 трактуем как null (новая запись)
                        val editId = rawId.takeIf { it >= 0 }
                        RouteFormScreen(navController = navController, editRouteId = editId)
                    }
                    composable(
                        route = Screen.RouteDetails.route,
                        arguments = listOf(navArgument("routeId") {
                            type = NavType.IntType
                        })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("routeId") ?: return@composable
                        RouteDetailsScreen(id, navController)
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
