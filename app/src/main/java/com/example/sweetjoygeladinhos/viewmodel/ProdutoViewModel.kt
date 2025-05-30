package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.FirestoreRepository
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> get() = _produtos

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> get() = _erro

    fun carregarProdutos() {
        viewModelScope.launch {
            val resultado = repository.getProdutos()
            if (resultado.isSuccess) {
                _produtos.value = resultado.getOrNull() ?: emptyList()
                _erro.value = null
            } else {
                _erro.value = resultado.exceptionOrNull()?.message ?: "Erro desconhecido"
            }
        }
    }

    fun adicionarProduto(produto: Produto) {
        viewModelScope.launch {
            val resultado = repository.addProduto(produto)
            if (resultado.isSuccess) {
                carregarProdutos()
            } else {
                _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao adicionar produto"
            }
        }
    }

    fun atualizarProduto(produto: Produto) {
        viewModelScope.launch {
            val resultado = repository.updateProduto(produto)
            if (resultado.isSuccess) {
                carregarProdutos()
            } else {
                _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao atualizar produto"
            }
        }
    }

    fun deletarProduto(produtoId: String) {
        viewModelScope.launch {
            val resultado = repository.deleteProduto(produtoId)
            if (resultado.isSuccess) {
                carregarProdutos()
            } else {
                _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao deletar produto"
            }
        }
    }
}
