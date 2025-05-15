package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto

@Dao
interface EstoqueDao {

    @Transaction
    @Query("SELECT * FROM EstoqueItem")
    suspend fun getAll(): List<EstoqueItemComProduto>

    @Query("SELECT * FROM EstoqueItem WHERE produtoId = :produtoId LIMIT 1")
    suspend fun getByProdutoId(produtoId: Long): EstoqueItem?

    @Insert
    suspend fun insertEstoqueItem(item: EstoqueItem)

    @Update
    suspend fun updateEstoqueItem(item: EstoqueItem)

    @Delete
    suspend fun deleteEstoqueItem(item: EstoqueItem)

    @Query("DELETE FROM EstoqueItem WHERE produtoId = :produtoId")
    suspend fun deleteByProdutoId(produtoId: Long)

    @Transaction
    suspend fun insert(produtoId: Long, quantidade: Int) {
        val existente = getByProdutoId(produtoId)
        if (existente != null) {
            val novaQuantidade = existente.quantidade + quantidade
            updateEstoqueItem(existente.copy(quantidade = novaQuantidade))
        } else {
            insertEstoqueItem(EstoqueItem(produtoId = produtoId, quantidade = quantidade))
        }
    }
}
