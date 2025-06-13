package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Promocao
import com.example.sweetjoygeladinhos.repository.PromocaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PromocaoViewModel(
    private val repository: PromocaoRepository = PromocaoRepository()
) : ViewModel() {

    private val _promocoes = MutableStateFlow<List<Promocao>>(emptyList())
    val promocoes: StateFlow<List<Promocao>> = _promocoes

    init {
        carregarPromocoes()
    }

    fun carregarPromocoes() {
        viewModelScope.launch {
            try {
                val lista = repository.obterPromocoes()
                _promocoes.value = lista
            } catch (e: Exception) {
                // Tratar erro se quiser
                _promocoes.value = emptyList()
            }
        }
    }

    fun salvarPromocao(promocao: Promocao, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                if (promocao.id.isBlank()) {
                    repository.adicionarPromocao(promocao)
                } else {
                    repository.atualizarPromocao(promocao)
                }
                carregarPromocoes()
                onComplete()
            } catch (e: Exception) {
                onComplete()
            }
        }
    }

    fun excluirPromocao(id: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.deletarPromocao(id)
                carregarPromocoes()
                onComplete()
            } catch (e: Exception) {
                onComplete()
            }
        }
    }


}
