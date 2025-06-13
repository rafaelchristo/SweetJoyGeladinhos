package com.example.sweetjoygeladinhos.model

data class EstoqueItemComProduto(
    val item: EstoqueItem = EstoqueItem(),
    val produto: Produto = Produto()
)
