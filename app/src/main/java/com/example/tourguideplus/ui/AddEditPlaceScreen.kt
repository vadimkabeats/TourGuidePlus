package com.example.tourguideplus.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tourguideplus.data.model.Place
import java.io.File
import java.io.FileOutputStream

// Топ-левел утилита копирования URI из галереи в папку приложения
private fun copyGalleryImageToAppStorage(context: Context, sourceUri: Uri): Uri? {
    return try {
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val outFile = File(picturesDir, "place_${System.currentTimeMillis()}.jpg")
            FileOutputStream(outFile).use { output -> input.copyTo(output) }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                outFile
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Топ-левел утилита сохранения Bitmap (с камеры) в папку приложения
private fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri {
    val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val outFile = File(picturesDir, "place_${System.currentTimeMillis()}.jpg")
    FileOutputStream(outFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        outFile
    )
}

@Composable
fun AddEditPlaceScreen(
    navController: NavController,
    editPlaceId: Int? = null,
    viewModel: PlaceViewModel = viewModel()
) {
    val context = LocalContext.current

    // Получаем все места из ViewModel
    val places by viewModel.places.collectAsState()

    // Находим редактируемое место (или null)
    val editingPlace = remember(editPlaceId, places) {
        editPlaceId?.let { id -> places.firstOrNull { it.id == id } }
    }

    // Инициализация состояний формы единожды на основе editingPlace
    var name by remember(editingPlace) { mutableStateOf(editingPlace?.name.orEmpty()) }
    var category by remember(editingPlace) { mutableStateOf(editingPlace?.category.orEmpty()) }
    var description by remember(editingPlace) { mutableStateOf(editingPlace?.description.orEmpty()) }
    var imageUri by remember(editingPlace) { mutableStateOf(editingPlace?.imageUri) }

    // Лончеры для галереи и камеры
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            copyGalleryImageToAppStorage(context, it)
                ?.toString()
                .also { copied -> imageUri = copied }
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            saveBitmapAndGetUri(context, it)
                .toString()
                .also { uriStr -> imageUri = uriStr }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (editingPlace == null) "Добавить место"
                        else "Редактировать место"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Отмена")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Превью фото
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
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
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка сохранения
            Button(
                onClick = {
                    val placeToSave = Place(
                        id = editingPlace?.id ?: 0,
                        name = name,
                        category = category,
                        description = description,
                        imageUri = imageUri,
                        isFavorite = editingPlace?.isFavorite ?: false,
                        latitude = editingPlace?.latitude,
                        longitude = editingPlace?.longitude
                    )
                    viewModel.upsert(placeToSave)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }
        }
    }
}
