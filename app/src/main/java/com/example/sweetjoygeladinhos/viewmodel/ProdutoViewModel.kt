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

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> get() = _carregando

    fun carregarProdutos() {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val resultado = repository.getProdutos()
                if (resultado.isSuccess) {
                    _produtos.value = resultado.getOrNull() ?: emptyList()
                    _erro.value = null
                } else {
                    _erro.value = resultado.exceptionOrNull()?.message ?: "Erro desconhecido"
                }
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao carregar produtos"
            } finally {
                _carregando.value = false
            }
        }
    }

    fun adicionarProduto(produto: Produto) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val resultado = repository.addProduto(produto)
                if (resultado.isSuccess) {
                    carregarProdutos()
                    _erro.value = null
                } else {
                    _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao adicionar produto"
                }
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao adicionar produto"
            } finally {
                _carregando.value = false
            }
        }
    }

    fun atualizarProduto(produto: Produto) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val resultado = repository.updateProduto(produto)
                if (resultado.isSuccess) {
                    carregarProdutos()
                    _erro.value = null
                } else {
                    _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao atualizar produto"
                }
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao atualizar produto"
            } finally {
                _carregando.value = false
            }
        }
    }

    fun deletarProduto(produtoId: String) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val resultado = repository.deleteProduto(produtoId)
                if (resultado.isSuccess) {
                    carregarProdutos()
                    _erro.value = null
                } else {
                    _erro.value = resultado.exceptionOrNull()?.message ?: "Erro ao deletar produto"
                }
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao deletar produto"
            } finally {
                _carregando.value = false
            }
        }
    }
}
