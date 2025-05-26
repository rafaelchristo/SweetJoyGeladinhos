package com.example.sweetjoygeladinhos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.ProdutoDatabase
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProdutoViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = ProdutoDatabase.getDatabase(application).produtoDao()
    val todosProdutos: Flow<List<Produto>> = produtoDao.getAll()

    fun adicionarProduto(produto: Produto) {
        viewModelScope.launch {
            produtoDao.insert(produto)
        }
    }

    fun deletarProduto(produto: Produto) {
        viewModelScope.launch {
            produtoDao.delete(produto)
        }
    }
}
