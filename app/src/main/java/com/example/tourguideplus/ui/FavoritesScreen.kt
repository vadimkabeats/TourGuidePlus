package com.example.tourguideplus.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tourguideplus.navigation.Screen

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: PlaceViewModel = viewModel()
) {
    val favorites by viewModel.favoritePlaces.collectAsState()

    if (favorites.isEmpty()) {
        // Заглушка, если нет избранного
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "У вас нет избранных мест.\nДобавьте их на экране «Места»",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            items(favorites) { place ->
                PlaceItem(
                    place = place,
                    onClick = {
                        navController.navigate(Screen.PlaceDetails.createRoute(place.id))
                    },
                    onToggleFavorite = {
                        // Снимаем флаг избранного
                        viewModel.upsert(place.copy(isFavorite = false))
                    }
                )
            }
        }
    }
}