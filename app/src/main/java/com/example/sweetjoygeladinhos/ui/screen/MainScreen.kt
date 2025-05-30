package com.example.sweetjoygeladinhos.ui.screens

import SweetJoyGeladinhosTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.PromocaoScreen
import com.example.sweetjoygeladinhos.ui.ReceitaScreen
import com.example.sweetjoygeladinhos.ui.VendasScreen
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screen.ProdutosScreen
import com.example.sweetjoygeladinhos.ui.screen.SplashScreen
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import com.example.sweetjoygeladinhos.ui.screens.GraficoVendasScreen
import com.example.sweetjoygeladinhos.viewmodel.ProdutoViewModel
import com.example.sweetjoygeladinhos.viewmodel.PromocaoViewModel

@Composable
fun MainScreen(navController: NavHostController, userViewModel: UserViewModel = viewModel()) {
    SweetJoyGeladinhosTheme {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController, userViewModel) }
            composable("home") { HomeScreen(navController, userViewModel) }
            composable("produtos") {
                val produtosViewModel: ProdutoViewModel = viewModel()
                ProdutosScreen(navController, produtosViewModel)
            }
            composable("estoque") { EstoqueScreen(navController) }
            composable("vendas") { VendasScreen(navController) }
            composable("pagamentos") { PagamentosScreen(navController) }
            composable("receitas") { ReceitaScreen() }
            composable("promocao") {
                val promocaoViewModel: PromocaoViewModel = viewModel()
                PromocaoScreen(navController, promocaoViewModel)
            }
            composable("graficoVendas") { GraficoVendasScreen(navController) }
        }
    }
}
