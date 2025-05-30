package com.example.sweetjoygeladinhos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.PromocaoScreen
import com.example.sweetjoygeladinhos.ui.VendasScreen
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screen.ProdutosScreen
import com.example.sweetjoygeladinhos.ui.screens.PagamentosScreen
import com.example.sweetjoygeladinhos.viewmodel.ProdutoViewModel
import com.example.sweetjoygeladinhos.viewmodel.PromocaoViewModel

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = "produtos") {
    NavHost(navController, startDestination) {
        composable("produtos") {
            val produtoViewModel: ProdutoViewModel = viewModel()
            ProdutosScreen(navController = navController, viewModel = produtoViewModel)
        }
        composable("estoque") { EstoqueScreen(navController = navController) }
        composable("vendas") { VendasScreen(navController = navController) }
        composable("pagamentos") { PagamentosScreen(navController = navController) }
        composable("promocao") {
            val promocaoViewModel: PromocaoViewModel = viewModel()
            PromocaoScreen(navController = navController, viewModel = promocaoViewModel)
        }
    }
}
