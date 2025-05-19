package com.example.sweetjoygeladinhos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.Venda

@Database(
    entities = [Produto::class, EstoqueItem::class, Venda::class , Receita::class],
    version = 5 , exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun estoqueDao(): EstoqueDao
    abstract fun vendaDao(): VendaDao
    abstract fun receitaDao(): ReceitaDao
}