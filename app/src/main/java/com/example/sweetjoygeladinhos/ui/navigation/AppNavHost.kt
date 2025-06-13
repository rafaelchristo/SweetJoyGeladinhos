package com.example.sweetjoygeladinhos.ui.navigation

import ProdutosScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.screen.*
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.viewmodel.*
import androidx.compose.runtime.getValue

@Composable
fun AppNavHost(
    navController: NavHostController
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

        composable("estoque") {
            val estoqueViewModel: EstoqueViewModel = viewModel()
            EstoqueScreen(viewModel = estoqueViewModel)
        }

        composable("vendas") {
            val vendaViewModel: VendaViewModel = viewModel()
            val estoqueViewModel: EstoqueViewModel = viewModel()
            val estoqueList by estoqueViewModel.estoque.collectAsState()

            VendasScreen(
                vendaViewModel = vendaViewModel,
                estoqueList = estoqueList
            )
        }

        composable("pagamentos") { PagamentosScreen(navController) }

        composable("promocao") {
            val promocaoViewModel: PromocaoViewModel = viewModel()
            PromocaoScreen(viewModel = promocaoViewModel)
        }

        composable("receita") {
            val receitaViewModel: ReceitaViewModel = viewModel()
            ReceitaScreen(viewModel = receitaViewModel)
        }

        // Telas Comuns
        composable("graficos") { GraficoVendasScreen(navController) }
        composable("relatorios") { RelatorioScreen() }
        composable("sobre") { SobreScreen() }

        // Tela de Pedidos — ViewModel sem factory (Firestore)
        composable("pedidos") {
            val pedidosViewModel: PedidosViewModel = viewModel()
            PedidosScreen(viewModel = pedidosViewModel)
        }
    }
}
