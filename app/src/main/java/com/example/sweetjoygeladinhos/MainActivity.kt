package com.example.sweetjoygeladinhos

import SweetJoyGeladinhosTheme
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.example.sweetjoygeladinhos.ui.screens.MainScreen
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.ui.navigation.AppNavHost
import com.example.sweetjoygeladinhos.utils.preencherComProdutosDeTeste
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)

        setContent {
            val navController = rememberNavController()
            MainScreen(navController = navController, database = AppDatabase.getInstance(this))
        }
        preencherComProdutosDeTeste(this)
    }

}







