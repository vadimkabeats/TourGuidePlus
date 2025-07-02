package com.example.tourguideplus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Определяем палитры светлой и тёмной темы с красными цветами
private val LightColorPalette = lightColors(
    primary = Color(0xFFD32F2F),        // Red700
    primaryVariant = Color(0xFFC62828), // Red800
    secondary = Color(0xFFFF5252),      // Red A200
    background = Color(0xFFF6F6F6),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColorPalette = darkColors(
    primary = Color(0xFFEF9A9A),        // Red200
    primaryVariant = Color(0xFFB71C1C), // Red900
    secondary = Color(0xFFFF5252),      // Red A200
    background = Color(0xFF121212),
    surface = Color(0xFF1F1B24),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// 2. Определяем типографику приложения
private val AppTypography = Typography(
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontSize = 12.sp
    )
)

// 3. Определяем формы (скругления), увеличив радиус для кнопок
private val AppShapes = Shapes(
    small = RoundedCornerShape(16.dp),  // используется для кнопок
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

/**
 * Основная тема приложения с красной палитрой.
 * Оборачивайте всё содержимое setContent { } в TourGuidePlusTheme { ... }
 */
@Composable
fun TourGuidePlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
