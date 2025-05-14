package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produto")
data class Produto(
    @PrimaryKey(autoGenerate = true) val produtoId: Long = 0,
    val nome: String,
    val sabor: String,
    val preco: Double,
    val categoria: String = "Gourmet" // valor padrão
)

