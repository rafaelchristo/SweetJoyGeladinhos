package com.example.sweetjoygeladinhos.model

import androidx.room.*
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.Produto

data class EstoqueItemComProduto(
    @Embedded val estoqueItem: EstoqueItem,
    @Relation(
        parentColumn = "produtoId",
        entityColumn = "produtoId"
    )
    val produto: Produto
)

