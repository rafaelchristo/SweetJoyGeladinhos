package com.example.sweetjoygeladinhos.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "promocao")
data class Promocao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val descricao: String,
    val validade: String,
    val imagemUri: String?
)