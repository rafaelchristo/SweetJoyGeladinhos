package com.example.sweetjoygeladinhos.model


data class Pedido(
    val id: String = "",
    val data: Long = System.currentTimeMillis(),
    val produtos: Map<String, Int> = emptyMap(), // id do produto -> quantidade
    val total: Double = 0.0
)