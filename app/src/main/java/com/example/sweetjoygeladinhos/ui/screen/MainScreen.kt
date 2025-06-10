package com.example.sweetjoygeladinhos.ui.screens

import ProdutosScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.example.sweetjoygeladinhos.ui.screen.*
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import com.example.sweetjoygeladinhos.ui.screen.PedidosScreen
import com.example.sweetjoygeladinhos.viewmodel.PedidosViewModel
import com.example.sweetjoygeladinhos.viewmodel.PedidosViewModelFactory
import com.example.sweetjoygeladinhos.SweetJoyApp

@Composable
fun MainScreen(
    navController: NavHostController,
    database: AppDatabase,  // <- adiciona este parâmetro
    userViewModel: UserViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("home") { HomeContent(navController, userViewModel) }
        composable("produtos") { ProdutosScreen(navController) }
        composable("estoque") { EstoqueScreen(navController) }
        composable("vendas") { VendasScreen(navController) }
        composable("pagamentos") { PagamentosScreen(navController) }
        composable("receitas") { ReceitaScreen(navController) }
        composable("promocao") { PromocaoScreen(navController) }

        composable("pedidos") {
            val produtoDao = SweetJoyApp.database.produtoDao()  // usa o banco vindo como parâmetro
            val pedidoDao =  SweetJoyApp.database.pedidoDao() // usa o banco vindo como parâmetro
            val estoqueDao =  SweetJoyApp.database.estoqueDao()  // usa o banco vindo como parâmetro
            val vendaDao =  SweetJoyApp.database.vendaDao()  // usa o banco vindo como parâmetro
            val factory = PedidosViewModelFactory(produtoDao, pedidoDao, estoqueDao, vendaDao)

            val pedidosViewModel: PedidosViewModel = viewModel(factory = factory)
            PedidosScreen(viewModel = pedidosViewModel)
        }
    }
}

