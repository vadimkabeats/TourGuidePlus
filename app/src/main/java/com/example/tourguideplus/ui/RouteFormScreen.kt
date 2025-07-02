package com.example.tourguideplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tourguideplus.data.model.RouteEntity
import com.example.tourguideplus.ui.viewmodel.RouteViewModel
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun RouteFormScreen(
    navController: NavController,
    editRouteId: Int? = null,
    placeViewModel: PlaceViewModel = viewModel(),
    routeViewModel: RouteViewModel = viewModel()
) {
    // все места для выбора
    val allPlaces by placeViewModel.places.collectAsState()
    // если редактируем — найдём текущий маршрут
    val routes by routeViewModel.routes.collectAsState()
    val editing = remember(editRouteId, routes) {
        editRouteId?.let { id ->
            routes.firstOrNull { it.route.id == id }
        }
    }

    // состояние названия и выбранных мест
    var name by remember(editing) { mutableStateOf(editing?.route?.name.orEmpty()) }
    val initialSelected = editing?.places?.map { it.id }?.toSet().orEmpty()
    var selectedIds by remember(editing) { mutableStateOf(initialSelected) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editing == null) "Новый маршрут" else "Редактировать маршрут")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название маршрута") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Выберите места:", style = MaterialTheme.typography.subtitle1)
            LazyColumn(Modifier.weight(1f)) {
                items(allPlaces) { place ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(place.name, Modifier.weight(1f))
                        Checkbox(
                            checked = selectedIds.contains(place.id),
                            onCheckedChange = { checked ->
                                selectedIds = if (checked)
                                    selectedIds + place.id
                                else
                                    selectedIds - place.id
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    // сохраним маршрут
                    val entity = RouteEntity(
                        id = editing?.route?.id ?: 0,
                        name = name
                    )
                    routeViewModel.upsert(entity, selectedIds.toList())
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}