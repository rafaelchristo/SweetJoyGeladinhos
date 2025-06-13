package com.example.sweetjoygeladinhos.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


@Composable
fun DebugScreen() {
    val context: Context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("DebugScreen", "Função descobrirVersaoBanco executada")
    }
}
