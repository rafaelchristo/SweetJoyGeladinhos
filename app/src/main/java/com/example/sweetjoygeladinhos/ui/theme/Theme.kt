package com.example.sweetjoygeladinhos.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SweetPink,
    onPrimary = Color.White,
    secondary = SweetPurple,
    onSecondary = Color.White,
    background = Color(0xFFFFF8F9),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun SweetJoyGeladinhosTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}