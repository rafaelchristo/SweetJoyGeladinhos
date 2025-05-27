package com.example.sweetjoygeladinhos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.Promocao
import com.example.sweetjoygeladinhos.model.Venda

@Database(
    entities = [Produto::class, EstoqueItem::class, Venda::class , Receita::class, Promocao::class],
    version = 7 , exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun estoqueDao(): EstoqueDao
    abstract fun vendaDao(): VendaDao
    abstract fun receitaDao(): ReceitaDao
    abstract fun promocaoDao(): PromocaoDao
}