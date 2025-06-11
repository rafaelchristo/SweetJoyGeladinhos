package com.example.sweetjoygeladinhos.ui.navigation

import ProdutosScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.example.sweetjoygeladinhos.ui.screen.*
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import com.example.sweetjoygeladinhos.ui.screen.PedidosScreen
import com.example.sweetjoygeladinhos.viewmodel.PedidosViewModel
import com.example.sweetjoygeladinhos.viewmodel.PedidosViewModelFactory

@Composable
fun AppNavHost(
    navController: NavHostController,
    database: AppDatabase
) {
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }

        composable("login") {
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable("home") {
            MainScreenWithDrawer(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        // Telas Admin
        composable("produtos") { ProdutosScreen(navController) }
        composable("estoque") { EstoqueScreen(navController) }
        composable("vendas") { VendasScreen(navController) }
        composable("pagamentos") { PagamentosScreen(navController) }
        composable("promocao") { PromocaoScreen(navController) }
        composable("receitas") { ReceitaScreen(navController) }

        // Telas Comuns
        composable("graficos") { GraficoVendasScreen(navController) }
        composable("relatorios") { RelatorioScreen() }
        //composable("debug") { DebugScreen() }
        composable("sobre") { SobreScreen() }

        // Tela de Pedidos com factory passando os DAOs necessários
        composable("pedidos") {
            val factory = PedidosViewModelFactory(
                produtoDao = SweetJoyApp.database.produtoDao(),
                pedidoDao = SweetJoyApp.database.pedidoDao(),
                estoqueDao = SweetJoyApp.database.estoqueDao(),
                vendaDao = SweetJoyApp.database.vendaDao()
            )
            val pedidosViewModel: PedidosViewModel = viewModel(factory = factory)
            PedidosScreen(viewModel = pedidosViewModel)
        }
    }
}