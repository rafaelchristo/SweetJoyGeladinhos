package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.FirestoreVendaRepository
import com.example.sweetjoygeladinhos.model.Venda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VendaViewModel : ViewModel() {

    private val repository = FirestoreVendaRepository()

    private val _vendas = MutableStateFlow<List<Venda>>(emptyList())
    val vendas: StateFlow<List<Venda>> get() = _vendas

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> get() = _erro

    fun carregarVendas() {
        viewModelScope.launch {
            try {
                val lista = repository.getVendas()
                _vendas.value = lista
                _erro.value = null
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro desconhecido"
            }
        }
    }

    fun adicionarVenda(venda: Venda) {
        viewModelScope.launch {
            try {
                repository.addVenda(venda)
                carregarVendas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao adicionar venda"
            }
        }
    }

    fun atualizarVenda(venda: Venda) {
        viewModelScope.launch {
            try {
                repository.updateVenda(venda)
                carregarVendas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao atualizar venda"
            }
        }
    }

    fun deletarVenda(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteVenda(id)
                carregarVendas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao deletar venda"
            }
        }
    }
}
