package com.example.sweetjoygeladinhos.model

import androidx.room.*

@Entity(
    tableName = "estoque_item",
    foreignKeys = [ForeignKey(
        entity = Produto::class,
        parentColumns = ["produtoId"],
        childColumns = ["produtoId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["produtoId"])]
)
data class EstoqueItem(
    @PrimaryKey(autoGenerate = true) val estoqueId: Long = 0,
    val produtoId: Long, // <-- Agora usamos o ID do produto
    val quantidade: Int
)