package com.example.sweetjoygeladinhos.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EstoqueItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "estoque_id") val estoqueId: Long = 0,

    @Embedded(prefix = "produto_")
    val produto: Produto,

    @ColumnInfo(name = "quantidade") val quantidade: Int
)