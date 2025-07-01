package com.example.tourguideplus.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tourguideplus.data.model.Place


@Composable
fun PlacesScreen(
    viewModel: PlaceViewModel = viewModel()
) {
    // Подписываемся на список мест
    val places by viewModel.places.collectAsState()

    // Основной LazyColumn
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(places) { place ->
            PlaceItem(
                place = place,
                onToggleFavorite = {
                    // Инвертируем флаг isFavorite
                    viewModel.upsert(place.copy(isFavorite = !place.isFavorite))
                },
                onClick = {
                    // TODO: навигация на детали
                }
            )
        }
    }
}

@Composable
fun PlaceItem(
    place: Place,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = place.category,
                    style = MaterialTheme.typography.caption
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = place.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (place.isFavorite) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = "Избранное"
                )
            }
        }
    }
}