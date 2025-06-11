package com.example.sweetjoygeladinhos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue

@Composable
fun MainScreenWithDrawer(navController: NavController, content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        "Gráficos" to "graficos",
        "Relatórios" to "relatorios",
        //"Debug" to "debug",
        "Sobre" to "sobre"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                Divider()
                menuItems.forEach { (title, route) ->
                    NavigationDrawerItem(
                        label = { Text(title) },
                        selected = false,
                        onClick = {
                            navController.navigate(route)
                        },
                        icon = {
                            when (route) {
                                "graficos" -> Icon(Icons.Default.ShowChart, contentDescription = null)
                                "relatorios" -> Icon(Icons.Default.Assessment, contentDescription = null)
                                //"debug" -> Icon(Icons.Default.Assessment, contentDescription = null)
                                "sobre" -> Icon(Icons.Default.Info, contentDescription = null)
                            }
                        }
                    )
                }
            }
        },
        content = {
            content()
        }
    )
}

