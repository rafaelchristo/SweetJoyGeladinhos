package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.model.UserType
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSelectionScreen(navController: NavController, userViewModel: UserViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Selecione o tipo de usuário") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                userViewModel.userType = UserType.ADMIN
                navController.navigate("home")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Entrar como Administrador")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                userViewModel.userType = UserType.CLIENT
                navController.navigate("home")
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Entrar como Cliente")
            }
        }
    }
}
