package com.example.sweetjoygeladinhos.utils

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory

fun descobrirVersaoBanco(context: Context, nomeDoBanco: String = "sweetjoy.db") {
    val config = SupportSQLiteOpenHelper.Configuration.builder(context)
        .name(nomeDoBanco)
        .callback(object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                // Não usado aqui
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
                // Não usado aqui
            }
        })
        .build()

    val helper = FrameworkSQLiteOpenHelperFactory().create(config)
    val versaoAtual = helper.readableDatabase.version

    Log.d("VersaoBanco", "Versão atual do banco de dados: $versaoAtual")
}