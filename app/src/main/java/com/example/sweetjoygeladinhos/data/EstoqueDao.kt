package com.example.sweetjoygeladinhos.data

import androidx.room.*
import com.example.sweetjoygeladinhos.model.EstoqueItem
import kotlinx.coroutines.flow.Flow

@Dao
interface EstoqueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: EstoqueItem)

    @Update
    suspend fun update(item: EstoqueItem)

    @Delete
    suspend fun delete(item: EstoqueItem)

    @Query("SELECT * FROM EstoqueItem")
    fun getAll(): Flow<List<EstoqueItem>>
}