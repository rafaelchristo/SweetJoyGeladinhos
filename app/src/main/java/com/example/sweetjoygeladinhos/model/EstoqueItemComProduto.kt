package com.example.sweetjoygeladinhos.model

import androidx.room.Embedded
import androidx.room.Relation

data class EstoqueItemComProduto(
    @Embedded val item: EstoqueItem,

    @Relation(
        parentColumn = "produtoId",
        entityColumn = "produtoId"
    )
    val produto: Produto
)
