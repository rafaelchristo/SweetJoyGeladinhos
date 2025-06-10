package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert
    suspend fun inserir(pedido: Pedido)

    @Query("SELECT * FROM pedido ORDER BY data DESC")
    fun listarTodos(): Flow<List<Pedido>>
}