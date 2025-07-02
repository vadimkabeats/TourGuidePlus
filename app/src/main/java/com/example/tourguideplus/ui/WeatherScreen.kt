package com.example.tourguideplus.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tourguideplus.ui.viewmodel.WeatherUiState
import com.example.tourguideplus.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController

@Composable
fun WeatherScreen(navController: NavController,
                  viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ввод города
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Город") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                if (cityInput.isNotBlank()) viewModel.fetch(cityInput.trim())
            }),
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка refresh
        IconButton(onClick = { viewModel.refresh() }) {
            Icon(Icons.Default.Refresh, contentDescription = "Обновить")
        }

        when (uiState) {
            is WeatherUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is WeatherUiState.Error -> {
                val msg = (uiState as WeatherUiState.Error).message
                Text(text = "Ошибка: $msg", color = MaterialTheme.colors.error)
            }
            is WeatherUiState.Success -> {
                val data = (uiState as WeatherUiState.Success).data
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = data.cityName, style = MaterialTheme.typography.h5)
                    // Время обновления
                    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        .format(Date(data.timestamp * 1000))
                    Text(text = "Обновлено: $time", style = MaterialTheme.typography.caption)
                    // Иконка и описание
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${data.weather.first().icon}@2x.png",
                            contentDescription = data.weather.first().description
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = data.weather.first().description.capitalize(), style = MaterialTheme.typography.h6)
                    }
                    // Температура
                    Text(text = "${data.main.temp}°C", style = MaterialTheme.typography.h3)
                    // Ветер
                    Text(text = "Ветер: ${data.wind.speed} м/с", style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}