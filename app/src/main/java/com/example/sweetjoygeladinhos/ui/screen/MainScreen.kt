package com.example.sweetjoygeladinhos.ui.screens

import ProdutosScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screen.PromocaoScreen
import com.example.sweetjoygeladinhos.ui.theme.SweetJoyGeladinhosTheme
import com.example.sweetjoygeladinhos.model.UserType
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel

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
            composable("produtos") { ProdutosScreen(navController) }
            composable("estoque") { EstoqueScreen(navController) }
            composable("vendas") { VendasScreen(navController) }
            composable("pagamentos") { PagamentosScreen(navController) }
            composable("receitas") { ReceitaScreen(navController) }
            composable("promocao") { PromocaoScreen(navController) }
        }
    }
}
