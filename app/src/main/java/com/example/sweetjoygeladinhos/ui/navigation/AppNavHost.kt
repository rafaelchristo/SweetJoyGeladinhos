package com.example.sweetjoygeladinhos.ui.navigation

import ProdutosScreen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.screen.MainScreenWithDrawer
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screens.GraficoVendasScreen
import com.example.sweetjoygeladinhos.ui.screen.PromocaoScreen
import com.example.sweetjoygeladinhos.ui.screen.RelatorioScreen
import com.example.sweetjoygeladinhos.ui.screen.SobreScreen
import com.example.sweetjoygeladinhos.ui.screen.SplashScreen
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController = navController, userViewModel = userViewModel) }

        composable("home") {
            MainScreenWithDrawer(navController, userViewModel)
        }

        // Rotas para telas de admin
        composable("produtos") { ProdutosScreen(navController) }
        composable("estoque") { EstoqueScreen(navController) }
        composable("vendas") { VendasScreen(navController) }
        composable("pagamentos") { PagamentosScreen(navController) }
        composable("promocao") { PromocaoScreen(navController) }
        composable("receitas") { ReceitaScreen(navController) }

        // Rotas comuns
        composable("graficos") { GraficoVendasScreen(navController) }
        composable("relatorios") { RelatorioScreen() }
        composable("sobre") { SobreScreen() }
    }
}
