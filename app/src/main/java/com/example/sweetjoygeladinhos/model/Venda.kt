// model/Venda.kt
package com.example.sweetjoygeladinhos.model

data class Venda(
    val id: String = "",       // aqui será o document ID do Firestore
    val produtoId: String = "", // agora como String, pois Firestore usa IDs string
    val quantidade: Int = 0,
    val dataVenda: String = "" // formato "dd/MM/yyyy"
)
