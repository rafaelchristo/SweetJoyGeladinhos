package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Receita
import com.example.sweetjoygeladinhos.repository.ReceitaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReceitaViewModel(
    private val repository: ReceitaRepository = ReceitaRepository()
) : ViewModel() {

    private val _receitas = MutableStateFlow<List<Receita>>(emptyList())
    val receitas: StateFlow<List<Receita>> = _receitas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        carregarReceitas()
    }

    fun carregarReceitas() {
        viewModelScope.launch {
            _loading.value = true
            _receitas.value = repository.obterReceitas()
            _loading.value = false
        }
    }

    fun salvarReceita(receita: Receita, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            repository.adicionarReceita(receita)
            carregarReceitas()
            _loading.value = false
            onComplete()
        }
    }

    fun deletarReceita(id: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            repository.deletarReceita(id)
            carregarReceitas()
            _loading.value = false
            onComplete()
        }
    }
}
