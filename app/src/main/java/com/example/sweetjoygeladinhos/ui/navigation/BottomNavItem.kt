package com.example.sweetjoygeladinhos.ui.navigation

sealed class BottomNavItem(val route: String, val label: String) {
    object Produtos : BottomNavItem("produtos", "Produtos")
    object Estoque : BottomNavItem("estoque", "Estoque")
    object Vendas : BottomNavItem("vendas", "Vendas")
    object Pagamentos : BottomNavItem("pagamentos", "Pagamentos")
    object Promocao : BottomNavItem("promocao", "Promocao")
}