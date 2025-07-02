package com.example.tourguideplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tourguideplus.ui.viewmodel.RouteViewModel

@Composable
fun RouteDetailsScreen(
    routeId: Int,
    navController: NavController,
    routeViewModel: RouteViewModel = viewModel()
) {
    val routes by routeViewModel.routes.collectAsState()
    val entry = routes.firstOrNull { it.route.id == routeId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(entry.route.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { inner ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            item {
                Text("Места в маршруте:", style = MaterialTheme.typography.subtitle1)
                Spacer(Modifier.height(8.dp))
            }
            items(entry.places) { place ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 2.dp
                ) {
                    Text(
                        text = place.name,
                        Modifier.padding(12.dp),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}