package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Produto(
    @PrimaryKey(autoGenerate = true) val produtoId: Int = 0,
    val nome: String,
    val sabor: String,
    val preco: Double
)