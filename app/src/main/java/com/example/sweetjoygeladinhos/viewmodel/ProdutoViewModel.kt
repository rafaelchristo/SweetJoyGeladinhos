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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            carregarProdutos()
        }
    }

    suspend fun carregarProdutos() {
        _isLoading.value = true
        try {
            _produtos.value = repository.obterProdutos()
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun adicionarProduto(produto: Produto): Boolean {
        return try {
            _isLoading.value = true
            repository.adicionarProduto(produto)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun deletarProduto(id: String): Boolean {
        return try {
            _isLoading.value = true
            repository.deletarProduto(id)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun atualizarProduto(produto: Produto): Boolean {
        return try {
            _isLoading.value = true
            repository.atualizarProduto(produto)
            carregarProdutos()
            true
        } catch (e: Exception) {
            false
        } finally {
            _isLoading.value = false
        }
    }
}
