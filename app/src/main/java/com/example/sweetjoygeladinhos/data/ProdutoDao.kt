package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun getAll(): Flow<List<Produto>>

    @Insert
    suspend fun insert(produto: Produto)

    @Update
    suspend fun update(produto: Produto)

    @Delete
    suspend fun delete(produto: Produto)

    @Query("SELECT * FROM Produto WHERE produtoId = :id LIMIT 1")
    suspend fun getById(id: Long): Produto?
}