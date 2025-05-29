package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.viewmodel.UserViewModel

@Composable
fun HomeScreen(navController: NavController, userViewModel: UserViewModel) {
    HomeContent(navController, userViewModel)
}