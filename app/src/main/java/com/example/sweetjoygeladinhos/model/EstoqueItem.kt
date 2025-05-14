package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Produto::class,
        parentColumns = ["produtoId"],
        childColumns = ["produtoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EstoqueItem(
    @PrimaryKey val produtoId: Long,
    val quantidade: Int
)
