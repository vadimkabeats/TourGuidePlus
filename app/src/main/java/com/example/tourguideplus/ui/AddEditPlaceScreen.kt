package com.example.tourguideplus.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat

// Копирует картинку из галереи в локальную папку приложения
private fun copyGalleryImageToAppStorage(context: Context, sourceUri: Uri): Uri? =
    try {
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

// Сохраняет Bitmap (с камеры) в локальную папку приложения
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
    val places by viewModel.places.collectAsState()

    val editingPlace = remember(editPlaceId, places) {
        editPlaceId?.let { id -> places.firstOrNull { it.id == id } }
    }

    var name by remember(editingPlace) { mutableStateOf(editingPlace?.name.orEmpty()) }
    var category by remember(editingPlace) { mutableStateOf(editingPlace?.category.orEmpty()) }
    var description by remember(editingPlace) { mutableStateOf(editingPlace?.description.orEmpty()) }
    var imageUri by remember(editingPlace) { mutableStateOf(editingPlace?.imageUri) }
    var previewBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Галерея
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            copyGalleryImageToAppStorage(context, it)
                ?.toString()
                .also { copied -> imageUri = copied }
        }
    }

    // Камера
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val uri = try {
                saveBitmapAndGetUri(context, it)
            } catch (e: Exception) {
                Log.e("AddEditPlace", "Ошибка при сохранении фото", e)
                Toast.makeText(context, "Не удалось сохранить фото", Toast.LENGTH_SHORT).show()
                null
            }
            if (uri != null) {
                imageUri = uri.toString()
            } else {
                previewBitmap = it.asImageBitmap()
            }
        }
    }
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = RequestPermission()
    ) { granted ->
        if (granted) {
            // если разрешение дали — запускаем камеру
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Нужен доступ к камере", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editingPlace == null) "Добавить место" else "Редактировать место")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Отмена")
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
            // Превью фото
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    previewBitmap != null -> Image(
                        bitmap = previewBitmap!!,
                        contentDescription = "Временное фото",
                        modifier = Modifier.fillMaxSize()
                    )
                    imageUri != null -> AsyncImage(
                        model = imageUri,
                        contentDescription = "Фото места",
                        modifier = Modifier.fillMaxSize()
                    )
                    else -> Text("Нет фото", style = MaterialTheme.typography.caption)
                }
            }

            // Кнопки выбора фото
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Из галереи")
                }
                Button(onClick = {
                    // Проверяем: есть ли уже разрешение?
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraLauncher.launch(null)
                    } else {
                        // Запрашиваем разрешение
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("С камеры")
                }
            }

            // Поля ввода
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Название") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Категория") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            Spacer(Modifier.weight(1f))

            Button(onClick = {
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
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Сохранить")
            }
        }
    }
}
