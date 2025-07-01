package com.example.tourguideplus.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController

@Composable
fun WeatherScreen(    navController: NavController) {
    Text(text = "Экран Погода", modifier = Modifier.padding(16.dp))
}