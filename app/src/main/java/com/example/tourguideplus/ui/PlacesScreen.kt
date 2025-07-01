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
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.tourguideplus.navigation.Screen



@Composable
fun PlacesScreen(
    navController: NavController,
    viewModel: PlaceViewModel = viewModel()
) {
    val places by viewModel.places.collectAsState()
    LazyColumn {
        items(places) { place ->
            PlaceItem(
                place = place,
                onToggleFavorite = { viewModel.upsert(place.copy(isFavorite = !place.isFavorite)) },
                onClick = {
                    navController.navigate(Screen.PlaceDetails.createRoute(place.id))
                }
            )
        }
    }
}

@Composable
fun PlaceItem(
    place: Place,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
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
            // Превью фото слева
            if (place.imageUri != null) {
                AsyncImage(
                    model = place.imageUri,
                    contentDescription = "Фото ${place.name}",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                // Плейсхолдер, если фото нет
                Box(
                    Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет фото", style = MaterialTheme.typography.caption)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2) Текстовая часть
            Column(modifier = Modifier.weight(1f)) {
                Text(text = place.name, style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(4.dp))
                Text(text = place.category, style = MaterialTheme.typography.caption)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = place.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }

            // 3) Кнопка избранного
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