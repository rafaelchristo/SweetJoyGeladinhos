package com.example.sweetjoygeladinhos.model

data class EstoqueItem(
    val id: String = "",         // Firestore usará o document ID, pode armazenar aqui
    val produtoId: String = "",
    val quantidade: Int = 0,
    val sabor: String = ""       // Adicione o campo 'sabor' se for necessário na tela
)