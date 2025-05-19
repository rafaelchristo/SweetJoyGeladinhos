package com.example.sweetjoygeladinhos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.*
import com.example.sweetjoygeladinhos.ui.screen.EstoqueScreen
import com.example.sweetjoygeladinhos.ui.screens.*
import com.example.sweetjoygeladinhos.ui.theme.SweetJoyGeladinhosTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializa o Firebase ANTES de usar FirebaseAuth
        FirebaseApp.initializeApp(this)

        setContent {
            SweetJoyGeladinhosTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("home") { HomeScreen(navController) }
                    composable("produtos") { ProdutosScreen(navController) }
                    composable("estoque") { EstoqueScreen(navController) }
                    composable("vendas") { VendasScreen(navController) }
                    composable("pagamentos") { PagamentosScreen(navController) }
                }
            }
        }
    }
}