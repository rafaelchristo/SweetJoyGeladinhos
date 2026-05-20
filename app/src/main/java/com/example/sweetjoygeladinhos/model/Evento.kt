package com.example.sweetjoygeladinhos.model

import com.google.firebase.firestore.DocumentId

data class Evento(
    @DocumentId val id: String = "",
    val nome: String = "",
    val data: Long = System.currentTimeMillis(),
    val itens: List<EventoItem> = emptyList()
)

data class EventoItem(
    val produtoId: String = "",
    val nomeProduto: String = "",
    val quantidade: Int = 0
)
