package com.example.sweetjoygeladinhos.model

data class Promocao(
    val id: String = "",  // ID do documento Firestore
    val titulo: String = "",
    val descricao: String = "",
    val validade: String = "",
    val imagemUri: String? = null
)