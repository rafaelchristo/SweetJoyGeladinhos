package com.example.sweetjoygeladinhos.model

data class Venda(
    val id: String = "",
    val produtos: Map<String, Int> = emptyMap(), // id do produto para quantidade
    val total: Double = 0.0,
    val dataVenda: Long = System.currentTimeMillis() // timestamp
)
