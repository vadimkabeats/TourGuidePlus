package com.example.tourguideplus.ui

import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.tourguideplus.data.model.Place
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun AddEditPlaceScreen(
    navController: NavController,
    editPlaceId: Int? = null,                  // ← если null — режим «добавление»
    viewModel: PlaceViewModel = viewModel()
) {
    val context = LocalContext.current

    //  Состояния полей формы
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    //
    LaunchedEffect(editPlaceId) {
        editPlaceId?.let { id ->
            val place = viewModel.places.value.firstOrNull { it.id == id }
            if (place != null) {
                name = place.name
                category = place.category
                description = place.description
                imageUri = place.imageUri
            } else {
                Toast.makeText(context, "Ошибка загрузки места", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun saveBitmapAndGetUri(context: android.content.Context, bitmap: Bitmap) =
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "place_${System.currentTimeMillis()}.jpg").also { file ->
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        }.let { file ->
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        }
    // Лончеры для фото
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri?.toString()
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val uri = saveBitmapAndGetUri(context, it)
            imageUri = uri.toString()
        }
    }

    // UI формы
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editPlaceId == null) "Добавить место" else "Редактировать место") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Отмена")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Превью фото
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Фото места",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Нет фото", style = MaterialTheme.typography.caption)
                }
            }
            // Кнопки выбора фото
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Из галереи")
                }
                Button(onClick = { cameraLauncher.launch(null) }) {
                    Text("С камеры")
                }
            }
            // Поля ввода
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Кнопка сохранения
            Button(
                onClick = {
                    // 6. Собираем объект Place с нужным id
                    val place = Place(
                        id          = editPlaceId ?: 0,
                        name        = name,
                        category    = category,
                        description = description,
                        imageUri    = imageUri
                    )
                    viewModel.upsert(place)
                    // После сохранения возвращаемся
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}
