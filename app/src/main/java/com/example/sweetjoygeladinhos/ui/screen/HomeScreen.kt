package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sweet Joy Geladinhos") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Menu Principal",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Apply weight here
                    HomeButton(
                        text = "Produtos",
                        onClick = { navController.navigate("produtos") },
                        modifier = Modifier.weight(1f)
                    )
                    HomeButton(
                        text = "Estoque",
                        onClick = { navController.navigate("estoque") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Apply weight here
                    HomeButton(
                        text = "Vendas",
                        onClick = { navController.navigate("vendas") },
                        modifier = Modifier.weight(1f)
                    )
                    HomeButton(
                        text = "Pagamentos",
                        onClick = { navController.navigate("pagamentos") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) { // Add modifier parameter
    Button(
        onClick = onClick,
        modifier = modifier // Use the passed modifier
            .aspectRatio(1f) // torna quadrado
            .fillMaxWidth(), // Ensure button fills the space given by weight
        shape = RoundedCornerShape(24.dp), // cantos arredondados
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
    }
}
