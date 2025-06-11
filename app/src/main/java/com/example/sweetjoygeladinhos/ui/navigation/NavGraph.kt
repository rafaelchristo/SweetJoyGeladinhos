package com.example.sweetjoygeladinhos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.screen.*
import com.example.sweetjoygeladinhos.ui.screens.GraficoVendasScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "graficos") {
        composable("graficos") { GraficoVendasScreen(navController)}
        composable("relatorios") { RelatorioScreen() }
        //composable("debug") { DebugScreen() }
        composable("sobre") { SobreScreen() }
    }
}