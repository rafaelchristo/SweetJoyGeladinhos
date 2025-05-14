package com.example.sweetjoygeladinhos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.screens.ProdutosScreen
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screens.VendasScreen
import com.example.sweetjoygeladinhos.ui.screens.PagamentosScreen

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = "produtos") {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("produtos") { ProdutosScreen() }
        composable("estoque") { EstoqueScreen() }
        composable("vendas") { VendasScreen() }
        composable("pagamentos") { PagamentosScreen() }
    }
}