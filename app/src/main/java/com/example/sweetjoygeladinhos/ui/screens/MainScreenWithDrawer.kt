package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sweetjoygeladinhos.ui.screens.HomeContent
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreenWithDrawer(navController: NavHostController, userViewModel: UserViewModel) {
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
                Spacer(modifier = Modifier.Companion.height(12.dp))
                Text(
                    "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.Companion.padding(16.dp)
                )
                Divider()
                menuItems.forEach { (title, route) ->
                    NavigationDrawerItem(
                        label = { Text(title) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(route)
                        },
                        icon = {
                            when (route) {
                                "graficos" -> Icon(
                                    Icons.Default.ShowChart,
                                    contentDescription = null
                                )

                                "relatorios" -> Icon(
                                    Icons.Default.Assessment,
                                    contentDescription = null
                                )
//                                "debug" -> Icon(
//                                    Icons.Default.Assessment,
//                                    contentDescription = null
//                                )

                                "sobre" -> Icon(Icons.Default.Info, contentDescription = null)
                            }
                        }
                    )
                }
            }
        },
        content = {
            HomeContent(navController, userViewModel)
        }
    )
}