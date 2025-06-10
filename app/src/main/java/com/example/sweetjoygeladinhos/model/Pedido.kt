package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val pedidoId: Long = 0L,
    val data: Long = System.currentTimeMillis(),
    val detalhes: String, // JSON, texto ou outra forma de armazenar itens e quantidades
    val total: Double
)