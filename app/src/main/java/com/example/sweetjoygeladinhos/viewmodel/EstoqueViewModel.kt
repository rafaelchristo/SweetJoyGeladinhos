package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.FirestoreEstoqueRepository
import com.example.sweetjoygeladinhos.model.EstoqueItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstoqueViewModel : ViewModel() {

    private val repository = FirestoreEstoqueRepository()

    private val _estoque = MutableStateFlow<List<EstoqueItem>>(emptyList())
    val estoque: StateFlow<List<EstoqueItem>> get() = _estoque

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> get() = _erro

    init {
        carregarEstoque()
    }

    fun carregarEstoque() {
        viewModelScope.launch {
            try {
                val itens = repository.getEstoque()
                _estoque.value = itens
            } catch (e: Exception) {
                _erro.value = "Erro ao carregar estoque: ${e.message}"
            }
        }
    }

    fun adicionarEstoqueItem(item: EstoqueItem) {
        viewModelScope.launch {
            try {
                repository.addEstoqueItem(item)
                carregarEstoque()
            } catch (e: Exception) {
                _erro.value = "Erro ao adicionar: ${e.message}"
            }
        }
    }

    fun atualizarEstoqueItem(item: EstoqueItem) {
        viewModelScope.launch {
            try {
                repository.updateEstoqueItem(item)
                carregarEstoque()
            } catch (e: Exception) {
                _erro.value = "Erro ao atualizar: ${e.message}"
            }
        }
    }

    fun excluirEstoqueItem(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteEstoqueItem(id)
                carregarEstoque()
            } catch (e: Exception) {
                _erro.value = "Erro ao excluir: ${e.message}"
            }
        }
    }
}
