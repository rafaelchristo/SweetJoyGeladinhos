package com.example.sweetjoygeladinhos

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.google.firebase.FirebaseApp

class SweetJoyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val MIGRATION_5_6 = object : Migration(5,6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS promocao (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        titulo TEXT NOT NULL,
                        descricao TEXT NOT NULL,
                        validade TEXT NOT NULL,
                        imagemUri TEXT
                    )
                """.trimIndent())
            }
        }

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "sweetjoy.db"
        ).fallbackToDestructiveMigration() // <--- adiciona isso
            .addMigrations(MIGRATION_5_6)
            .build()
        FirebaseApp.initializeApp(this)

        val produtoDao = database.produtoDao()
        val estoqueDao = database.estoqueDao()
        val vendaDao = database.vendaDao()
    }
}
