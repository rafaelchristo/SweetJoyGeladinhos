package com.example.sweetjoygeladinhos

import SweetJoyGeladinhosTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.sweetjoygeladinhos.ui.navigation.AppNavHost
import com.example.sweetjoygeladinhos.utils.preencherComProdutosDeTeste

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se já preencheu os produtos de teste
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val jaPreencheu = sharedPreferences.getBoolean("produtos_teste_inseridos", false)

        if (!jaPreencheu) {
            preencherComProdutosDeTeste(this)

            // Marca como preenchido
            sharedPreferences.edit().putBoolean("produtos_teste_inseridos", true).apply()
        }

        setContent {
            SweetJoyGeladinhosTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)  // <-- Aqui, chama o AppNavHost
            }
        }
    }
}
