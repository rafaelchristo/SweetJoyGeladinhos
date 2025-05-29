// data/VendaDao.kt

package com.example.sweetjoygeladinhos.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.sweetjoygeladinhos.model.Venda

@Dao
interface VendaDao {
    @Insert
    suspend fun insert(venda: Venda)

    @Query("SELECT * FROM Venda ORDER BY dataVenda DESC")
    suspend fun getAll(): List<Venda>

    @Update
    suspend fun update(venda: Venda)

    @Delete
    suspend fun delete(venda: Venda)
}
