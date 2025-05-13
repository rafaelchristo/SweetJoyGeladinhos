package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Insert
    suspend fun insert(produto: Produto)

    @Update
    suspend fun update(produto: Produto)

    @Delete
    suspend fun delete(produto: Produto)

    @Query("SELECT * FROM produto")
    suspend fun getAll(): List<Produto>


}