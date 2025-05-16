package com.example.sweetjoygeladinhos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.*
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen // Assuming EstoqueScreen might need NavController
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.ui.theme.SweetJoyGeladinhosTheme
import androidx.navigation.NavController // Import NavController


@OptIn(ExperimentalMaterial3Api::class) // It's good practice to use OptIn for experimental APIs
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // SweetJoyGeladinhosTheme should wrap NavHost or be applied at a lower level if needed
            SweetJoyGeladinhosTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") { SplashScreen(navController) }
                    composable("home") { HomeScreen(navController) } // Pass navController
                    composable("produtos") { ProdutosScreen(navController) } // Pass navController
                    composable("estoque") { EstoqueScreen(navController) } // Pass navController
                    composable("vendas") { VendasScreen(navController) } // Pass navController
                    composable("pagamentos") { PagamentosScreen(navController) } // Pass navController
                }
            }
        }
    }
}