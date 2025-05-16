package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController // Import rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagamentosScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pagamentos") })
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            Text("Controle de pagamentos aqui")
        }
    }
}

@Preview
@Composable
fun PreviewPagamentosScreen() {
    // Create a NavController instance for the preview
    val navController = rememberNavController()
    PagamentosScreen(navController = navController) // Pass the navController
}