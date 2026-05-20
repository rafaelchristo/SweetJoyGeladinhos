package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Evento
import com.example.sweetjoygeladinhos.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventoViewModel : ViewModel() {
    private val repository = EventoRepository()

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando

    init {
        carregarEventos()
    }

    fun carregarEventos() {
        viewModelScope.launch {
            _carregando.value = true
            try {
                _eventos.value = repository.obterEventos()
            } catch (e: Exception) {
                // Tratar erro
            } finally {
                _carregando.value = false
            }
        }
    }

    fun criarEvento(evento: Evento, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.criarEvento(evento)
                carregarEventos()
                onSuccess()
            } catch (e: Exception) {
                // Tratar erro
            }
        }
    }
}
