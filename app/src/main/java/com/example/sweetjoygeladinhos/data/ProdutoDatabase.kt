package com.example.sweetjoygeladinhos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sweetjoygeladinhos.model.Produto

@Database(entities = [Produto::class], version = 1)
abstract class ProdutoDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCE: ProdutoDatabase? = null

        fun getDatabase(context: Context): ProdutoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProdutoDatabase::class.java,
                    "produto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
