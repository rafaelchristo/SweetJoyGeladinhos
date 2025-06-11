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
        val MIGRATION_X_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adiciona a nova coluna 'quantidade' com valor padrão 0
                database.execSQL("ALTER TABLE receitas ADD COLUMN quantidade INTEGER NOT NULL DEFAULT 0")
            }
        }

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "sweetjoy.db"
        ).fallbackToDestructiveMigration() // <--- adiciona isso
            .addMigrations(MIGRATION_X_9)
            .build()
        FirebaseApp.initializeApp(this)

    }
}
