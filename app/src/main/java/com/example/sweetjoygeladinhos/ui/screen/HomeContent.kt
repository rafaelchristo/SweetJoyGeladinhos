package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(navController: NavController, userViewModel: UserViewModel) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val oneTapClient = Identity.getSignInClient(LocalContext.current)

    var showLogoutDialog by remember { mutableStateOf(false) }

    // Menu apenas para administradores
    val adminMenu = listOf(
        "Produtos" to "produtos",
        "Estoque" to "estoque",
        "Vendas" to "vendas",
        "Pagamentos" to "pagamentos",
        "Receitas" to "receitas",
        "Promoção" to "promocao",
        "Fazer Pedido" to "pedidos"
    )

    val menuItems = adminMenu

    val softPink = Color(0xFFFFC1CC)
    val darkRose = Color(0xFF8B1E3F)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sweet Joy Geladinhos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = softPink,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Menu Principal",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = darkRose
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                menuItems.forEach { (text, route) ->
                    Button(
                        onClick = { navController.navigate(route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 72.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = softPink,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Deseja sair?", color = darkRose) },
            text = { Text("Você será desconectado.") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    firebaseAuth.signOut()
                    oneTapClient.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }) {
                    Text("Sair", color = softPink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}
