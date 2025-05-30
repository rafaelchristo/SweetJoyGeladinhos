package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.FirestorePromocaoRepository
import com.example.sweetjoygeladinhos.model.Promocao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PromocaoViewModel : ViewModel() {

    private val repository = FirestorePromocaoRepository()

    private val _promocoes = MutableStateFlow<List<Promocao>>(emptyList())
    val promocoes: StateFlow<List<Promocao>> get() = _promocoes

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> get() = _erro

    init {
        carregarPromocoes()
    }

    fun carregarPromocoes() {
        viewModelScope.launch {
            try {
                val lista = repository.getPromocoes()
                _promocoes.value = lista
                _erro.value = null
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro desconhecido"
            }
        }
    }

    fun adicionarPromocao(promocao: Promocao) {
        viewModelScope.launch {
            try {
                repository.addPromocao(promocao)
                carregarPromocoes()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao adicionar promoção"
            }
        }
    }

    fun atualizarPromocao(promocao: Promocao) {
        viewModelScope.launch {
            try {
                repository.updatePromocao(promocao)
                carregarPromocoes()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao atualizar promoção"
            }
        }
    }

    fun deletarPromocao(id: String) {
        viewModelScope.launch {
            try {
                repository.deletePromocao(id)
                carregarPromocoes()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao deletar promoção"
            }
        }
    }
}
