package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.FirestoreReceitaRepository
import com.example.sweetjoygeladinhos.model.Receita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReceitaViewModel : ViewModel() {

    private val repository = FirestoreReceitaRepository()

    private val _receitas = MutableStateFlow<List<Receita>>(emptyList())
    val receitas: StateFlow<List<Receita>> = _receitas

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro

    fun carregarReceitas() {
        viewModelScope.launch {
            try {
                _receitas.value = repository.getReceitas()
                _erro.value = null
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro desconhecido"
            }
        }
    }

    fun adicionarReceita(receita: Receita) {
        viewModelScope.launch {
            try {
                repository.addReceita(receita)
                carregarReceitas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao adicionar receita"
            }
        }
    }

    fun atualizarReceita(receita: Receita) {
        viewModelScope.launch {
            try {
                repository.updateReceita(receita)
                carregarReceitas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao atualizar receita"
            }
        }
    }

    fun deletarReceita(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteReceita(id)
                carregarReceitas()
            } catch (e: Exception) {
                _erro.value = e.message ?: "Erro ao deletar receita"
            }
        }
    }
}
