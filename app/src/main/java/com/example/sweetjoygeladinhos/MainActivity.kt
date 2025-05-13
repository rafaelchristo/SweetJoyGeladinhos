package com.example.sweetjoygeladinhos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.*
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.ui.theme.SweetJoyGeladinhosTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SweetJoyGeladinhosTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController = navController) // <- Certifique-se de que a função aceita esse parâmetro
                    }
                    composable("produtos") { ProdutosScreen() }
                    composable("estoque") { EstoqueScreen() }
                    composable("vendas") { VendasScreen() }
                    composable("pagamentos") { PagamentosScreen() }
                }
            }
        }
    }
}