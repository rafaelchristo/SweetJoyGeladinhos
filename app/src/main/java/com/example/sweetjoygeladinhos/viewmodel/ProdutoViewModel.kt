package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {
    private val repository = ProdutoRepository()

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    init {
        viewModelScope.launch {
            carregarProdutos()
        }
    }

    // Carrega a lista de produtos do repositório
    suspend fun carregarProdutos() {
        _produtos.value = repository.obterProdutos()
    }

    // Adiciona produto e retorna sucesso/falha
    suspend fun adicionarProduto(produto: Produto): Boolean {
        return try {
            repository.adicionarProduto(produto)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Deleta produto pelo id e retorna sucesso/falha
    suspend fun deletarProduto(id: String): Boolean {
        return try {
            repository.deletarProduto(id)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Atualiza produto e retorna sucesso/falha
    suspend fun atualizarProduto(produto: Produto): Boolean {
        return try {
            repository.atualizarProduto(produto)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        }
    }
}
