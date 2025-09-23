package com.example.sweetjoygeladinhos.model

data class EstoqueItemComProduto(
    val item: EstoqueItem,
    val produto: Produto,
    // campo para armazenar a quantidade editada no UI antes do envio
    var quantidadeEditada: Int = item.quantidade
)