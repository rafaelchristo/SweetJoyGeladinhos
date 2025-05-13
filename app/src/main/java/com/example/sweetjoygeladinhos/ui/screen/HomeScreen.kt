package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.sweetjoygeladinhos.R
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.material.icons.filled.Storefront




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProdutos: () -> Unit = {},
    onNavigateToEstoque: () -> Unit = {},
    onNavigateToVendas: () -> Unit = {},
    onNavigateToPagamentos: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SweetJoy Geladinhos", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Logo ou imagem
            Image(
                painter = painterResource(id = R.drawable.logo), // substitua por sua imagem ou remova se não tiver
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Gerencie seus Geladinhos com estilo!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botões de navegação
            NavigationButton("Produtos", Icons.Default.LocalGroceryStore, onNavigateToProdutos)
            NavigationButton("Estoque", Icons.Default.Inventory, onNavigateToEstoque)
            NavigationButton("Vendas", Icons.Default.ShoppingCart, onNavigateToVendas)
            NavigationButton("Pagamentos", Icons.Default.Money, onNavigateToPagamentos)
        }
    }
}

@Composable
fun NavigationButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 16.sp)
    }
}

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SweetJoy Geladinhos") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibe uma logo se houver imagem em res/drawable/logo.png
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo SweetJoy",
                modifier = Modifier.size(120.dp),

            )

            Spacer(modifier = Modifier.height(32.dp))

            ButtonWithIcon("Gerenciar Produtos", Icons.Default.Storefront) {
                navController.navigate("produtos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithIcon("Controle de Estoque", Icons.Default.Inventory) {
                navController.navigate("estoque")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithIcon("Registrar Vendas", Icons.Default.ShoppingCart) {
                navController.navigate("vendas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithIcon("Pagamentos", Icons.Default.Money) {
                navController.navigate("pagamentos")
            }
        }
    }
}

@Composable
fun ButtonWithIcon(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.padding(end = 8.dp))
        Text(text)
    }
}