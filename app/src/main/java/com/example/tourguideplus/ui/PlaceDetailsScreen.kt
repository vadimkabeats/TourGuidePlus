package com.example.tourguideplus.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import com.example.tourguideplus.ui.viewmodel.PlaceViewModel
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
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val wikiExtract by viewModel.wikiExtract.collectAsState()
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

            // кнопка «Открыть на карте»
            Button(
                onClick = {
                         val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(place.name)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                                           addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    val pm = context.packageManager
                    if (mapIntent.resolveActivity(pm) != null) {
                        context.startActivity(mapIntent)
                    } else {
                        // 2) Иначе — открываем в браузере веб-версию Google Maps
                        val webUri = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=${Uri.encode(place.name)}"
                        )
                        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                        context.startActivity(webIntent)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Открыть на карте")
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
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Удалить", color = Color.White)
            }
            Button(
                onClick = { viewModel.loadWiki(place.name) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Получить краткое описание")
            }
            wikiExtract?.let { text ->
                // Если текст пустой, показываем заглушку
                val display = if (text.isBlank()) "Информация не найдена" else text
                Text(
                    text = display,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить место?") },
            text  = { Text("Вы уверены, что хотите удалить «${place.name}»?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        viewModel.delete(place)
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                }) {
                    Text("Да, удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

