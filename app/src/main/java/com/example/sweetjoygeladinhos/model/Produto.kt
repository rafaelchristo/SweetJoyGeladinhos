package com.example.sweetjoygeladinhos.model

data class Produto(
    val id: String = "", // ID do documento no Firestore
    val nome: String = "",
    val sabor: String = "",
    val preco: Double = 0.0,
    val categoria: String = "Gourmet",
    val imagemUri: String? = null
)
