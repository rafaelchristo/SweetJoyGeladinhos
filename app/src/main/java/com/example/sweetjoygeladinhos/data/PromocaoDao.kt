package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.Promocao

@Dao
interface PromocaoDao {
    @Query("SELECT * FROM promocao")
    suspend fun getAll(): List<Promocao>

    @Insert
    suspend fun insert(promocao: Promocao)

    @Update
    suspend fun update(promocao: Promocao)

    @Delete
    suspend fun delete(promocao: Promocao)
}