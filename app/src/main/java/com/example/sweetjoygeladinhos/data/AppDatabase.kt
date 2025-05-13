package com.example.sweetjoygeladinhos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.EstoqueItem

@Database(entities = [Produto::class, EstoqueItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun estoqueDao(): EstoqueDao
}