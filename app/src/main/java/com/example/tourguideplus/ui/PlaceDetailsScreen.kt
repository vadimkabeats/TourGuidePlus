package com.example.tourguideplus.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tourguideplus.navigation.Screen
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
fun PlaceDetailsScreen(
    placeId: Int,
    navController: NavController,
    viewModel: PlaceViewModel = viewModel()
) {
    // Получаем все места из ViewModel и находим нужное
    val places by viewModel.places.collectAsState()
    val place = places.firstOrNull { it.id == placeId } ?: return
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
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
            //  Фото
            if (place.imageUri != null) {
                AsyncImage(
                    model = place.imageUri,
                    contentDescription = "Фото места",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            //  Категория и описание
            Text(text = "Категория: ${place.category}", style = MaterialTheme.typography.subtitle1)
            Text(text = place.description, style = MaterialTheme.typography.body1)

            // Координаты и кнопка «Открыть на карте»
            if (place.latitude != null && place.longitude != null) {
                Button(onClick = {
                    val geoUri = Uri.parse("geo:${place.latitude},${place.longitude}?q=${place.latitude},${place.longitude}(${Uri.encode(place.name)})")
                    navController.context.startActivity(
                        Intent(Intent.ACTION_VIEW, geoUri)
                    )
                }) {
                    Text("Открыть на карте")
                }
            }

            //  Кнопка «Редактировать»
            Button(
                onClick = {
                    navController.navigate(
                        Screen.PlaceForm.createRoute(place.id)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать")
            }
            Button(
                onClick = {
                    scope.launch {
                        viewModel.delete(place)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Удалить", color = Color.White)
            }
        }
    }
}
