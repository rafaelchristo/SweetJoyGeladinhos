package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.R
import com.example.sweetjoygeladinhos.model.UserType
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var selectedRole by remember { mutableStateOf(UserType.CLIENT) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 👉 Aqui está a ilustração no topo
                Image(
                    painter = painterResource(id = R.drawable.login_illustration), // substitua com sua imagem
                    contentDescription = "Login Illustration",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )

                Text(
                    text = "Escolha o seu perfil",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { selectedRole = UserType.ADMIN },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selectedRole == UserType.ADMIN) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Administrador",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Admin")
                    }

                    OutlinedButton(
                        onClick = { selectedRole = UserType.CLIENT },
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selectedRole == UserType.CLIENT) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Cliente",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cliente")
                    }
                }

                Button(
                    onClick = {
                        userViewModel.userType = selectedRole
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Entrar como ${selectedRole.name.lowercase().replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
