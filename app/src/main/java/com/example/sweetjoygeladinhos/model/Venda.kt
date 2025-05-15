// model/Venda.kt
package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Venda(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val produtoId: Long,
    val quantidade: Int,
    val dataVenda: String // ✅ data como String (formato "dd/MM/yyyy")
)
