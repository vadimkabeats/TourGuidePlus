package com.example.tourguideplus.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tourguideplus.data.model.RouteWithPlaces
import com.example.tourguideplus.navigation.Screen
import com.example.tourguideplus.ui.viewmodel.RouteViewModel

@Composable
fun RoutesScreen(
    navController: NavController,
    viewModel: RouteViewModel = viewModel()
) {
    // StateFlow списка маршрутов
    val routes by viewModel.routes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Переход в форму создания (null → новый маршрут)
                navController.navigate(Screen.RouteForm.createRoute(null))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить маршрут")
            }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            items(routes) { rw ->
                RouteItem(
                    routeWithPlaces = rw,
                    onClick = {
                        // Переход в детали маршрута
                        navController.navigate(Screen.RouteDetails.createRoute(rw.route.id))
                    },
                    onEdit = {
                        // Переход в форму редактирования
                        navController.navigate(Screen.RouteForm.createRoute(rw.route.id))
                    },
                    onDelete = {
                        viewModel.delete(rw.route)
                    }
                )
            }
        }
    }
}

@Composable
fun RouteItem(
    routeWithPlaces: RouteWithPlaces,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = routeWithPlaces.route.name, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Мест в маршруте: ${routeWithPlaces.places.size}",
                    style = MaterialTheme.typography.body2
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Add, contentDescription = "Редактировать")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
        }
    }
}