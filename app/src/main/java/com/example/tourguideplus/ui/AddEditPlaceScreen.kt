package com.example.tourguideplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tourguideplus.data.model.Place


@Composable
fun AddEditPlaceScreen(
    navController: NavController,
    viewModel: PlaceViewModel = viewModel()
) {
    // локальные состояния для полей формы
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // TODO: добавим imageUri, latitude, longitude позже

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Добавить место") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    // сохраняем новое место
                    viewModel.upsert(
                        Place(
                            name = name,
                            category = category,
                            description = description,
                            imageUri = null
                        )
                    )
                    navController.popBackStack() // возвращаемся на список
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}