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
import androidx.navigation.compose.*
import com.example.tourguideplus.navigation.Screen
import com.example.tourguideplus.ui.*
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.filled.Add



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val items = listOf(
                Screen.Places, Screen.Routes, Screen.Favorites, Screen.Weather, Screen.Help
            )
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        items.forEach { screen ->
                            BottomNavigationItem(
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (screen is Screen.Help) {
                                        startActivity(Intent(this@MainActivity, HelpActivity::class.java))
                                    } else {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = when (screen) {
                                            is Screen.Places -> Icons.Default.Place
                                            is Screen.Routes -> Icons.Default.Map
                                            is Screen.Favorites -> Icons.Default.Favorite
                                            is Screen.Weather -> Icons.Default.Cloud
                                            is Screen.Help -> Icons.Default.Info
                                            is Screen.PlaceForm -> Icons.Default.Add
                                        },
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(screen.title) }
                            )
                        }
                    }
                },
                floatingActionButton = {
                    // показываем только на экране Places
                    if (currentRoute == Screen.Places.route) {
                        FloatingActionButton(onClick = {
                            navController.navigate(Screen.PlaceForm.route)
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
                    composable(Screen.Places.route) { PlacesScreen() }
                    composable(Screen.Routes.route) { RoutesScreen() }
                    composable(Screen.Favorites.route) { FavoritesScreen() }
                    composable(Screen.Weather.route) { WeatherScreen() }
                    composable(Screen.PlaceForm.route) {
                        AddEditPlaceScreen(navController = navController)
                    }
                }
            }
        }
    }
}