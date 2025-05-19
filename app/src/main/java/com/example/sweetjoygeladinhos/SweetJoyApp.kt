package com.example.sweetjoygeladinhos

import android.app.Application
import androidx.room.Room
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.google.firebase.FirebaseApp

class SweetJoyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "sweetjoy.db"
        ).fallbackToDestructiveMigration() // <--- adiciona isso
            .build()
        FirebaseApp.initializeApp(this)

        val produtoDao = database.produtoDao()
        val estoqueDao = database.estoqueDao()
        val vendaDao = database.vendaDao()
    }
}
