package com.example.sweetjoygeladinhos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sweetjoygeladinhos.model.*

@Database(
    entities = [
        Produto::class,
        EstoqueItem::class,
        Venda::class,
        Receita::class,
        Promocao::class,
        Pedido::class // 👈 aqui
    ],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun estoqueDao(): EstoqueDao
    abstract fun vendaDao(): VendaDao
    abstract fun receitaDao(): ReceitaDao
    abstract fun promocaoDao(): PromocaoDao
    abstract fun pedidoDao(): PedidoDao // 👈 aqui


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sweetjoygeladinhos_db"
                )
                    .fallbackToDestructiveMigration() // cuidado: destrói dados em migrações não tratadas
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
