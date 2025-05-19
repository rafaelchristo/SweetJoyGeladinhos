package com.example.sweetjoygeladinhos.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ReceitaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(receita: Receita)

    @Update
    suspend fun atualizar(receita: Receita)

    @Delete
    suspend fun deletar(receita: Receita)

    @Query("SELECT * FROM receitas ORDER BY nome ASC")
    fun listarTodas(): kotlinx.coroutines.flow.Flow<List<Receita>>
}
