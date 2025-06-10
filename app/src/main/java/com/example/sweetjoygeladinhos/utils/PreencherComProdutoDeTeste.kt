package com.example.sweetjoygeladinhos.utils

import android.content.Context
import com.example.sweetjoygeladinhos.data.AppDatabase
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun preencherComProdutosDeTeste(context: Context) {
    val db = AppDatabase.getInstance(context)
    CoroutineScope(Dispatchers.IO).launch {
        if (db.produtoDao().getAllNow().isEmpty()) {
            db.produtoDao().insert(Produto(nome = "Morango", sabor ="Branco", preco = 2.50))
            db.produtoDao().insert(Produto(nome = "Chocolate",sabor ="Preto", preco = 3.00))
            db.produtoDao().insert(Produto(nome = "Uva",sabor ="Roxo", preco = 2.30))
            db.produtoDao().insert(Produto(nome = "Limão",sabor ="Verde", preco = 2.00))
        }
    }
}
