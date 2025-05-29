package com.example.sweetjoygeladinhos.ui.theme

import PinkLightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun SweetJoyGeladinhosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PinkLightColorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
