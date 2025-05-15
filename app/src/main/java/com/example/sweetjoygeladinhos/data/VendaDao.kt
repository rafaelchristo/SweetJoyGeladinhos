// data/VendaDao.kt
package com.example.sweetjoygeladinhos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sweetjoygeladinhos.model.Venda

@Dao
interface VendaDao {
    @Insert
    suspend fun insert(venda: Venda)

    @Query("SELECT * FROM Venda ORDER BY dataVenda DESC")
    suspend fun getAll(): List<Venda>
}
