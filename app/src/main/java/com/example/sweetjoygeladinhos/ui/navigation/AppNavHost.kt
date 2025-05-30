package com.example.sweetjoygeladinhos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.PromocaoScreen
import com.example.sweetjoygeladinhos.ui.ReceitaScreen
import com.example.sweetjoygeladinhos.ui.VendasScreen
import com.example.sweetjoygeladinhos.ui.screen.*
import com.example.sweetjoygeladinhos.ui.screens.GraficoVendasScreen
import com.example.sweetjoygeladinhos.ui.screens.LoginScreen
import com.example.sweetjoygeladinhos.ui.screens.PagamentosScreen
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel
import com.example.sweetjoygeladinhos.viewmodel.ProdutoViewModel
import com.example.sweetjoygeladinhos.viewmodel.PromocaoViewModel
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import com.example.sweetjoygeladinhos.viewmodel.VendaViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    val userViewModel: UserViewModel = viewModel()
    val produtoViewModel: ProdutoViewModel = viewModel()
    val estoqueViewModel: EstoqueViewModel = viewModel()
    val vendaViewModel: VendaViewModel = viewModel()
    val promocaoViewModel: PromocaoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("home") { MainScreenWithDrawer(navController, userViewModel) }

        // Rotas para telas de admin
        composable("produtos") { ProdutosScreen(navController, produtoViewModel) }
        composable("estoque") { EstoqueScreen(navController, estoqueViewModel) }
        composable("vendas") { VendasScreen(navController, vendaViewModel) }
        composable("pagamentos") { PagamentosScreen(navController) }
        composable("promocao") { PromocaoScreen(navController, promocaoViewModel) }
        composable("receitas") { ReceitaScreen() }

        // Rotas comuns
        composable("graficos") { GraficoVendasScreen(navController) }
        composable("relatorios") { RelatorioScreen() }
        composable("sobre") { SobreScreen() }
    }
}
