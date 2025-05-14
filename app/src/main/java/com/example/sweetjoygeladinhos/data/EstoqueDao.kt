package com.example.sweetjoygeladinhos.data


import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import androidx.room.*
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.Produto


@Dao
interface EstoqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstoqueItem(estoqueItem: EstoqueItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduto(produto: Produto): Long

    @Transaction
    suspend fun insert(produto: Produto, quantidade: Int) {
        val produtoId = insertProduto(produto)
        insertEstoqueItem(EstoqueItem(produtoId = produtoId, quantidade = quantidade))
    }

    @Transaction
    @Query("SELECT * FROM estoque_item")
    suspend fun getAll(): List<EstoqueItemComProduto>
}