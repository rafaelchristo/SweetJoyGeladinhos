package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Venda
import com.example.sweetjoygeladinhos.repository.VendaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VendaViewModel : ViewModel() {
    private val repository = VendaRepository()

    private val _vendas = MutableStateFlow<List<Venda>>(emptyList())
    val vendas: StateFlow<List<Venda>> = _vendas

    init {
        carregarVendas()
    }

    fun carregarVendas() {
        viewModelScope.launch {
            _vendas.value = repository.obterVendas()
        }
    }

    fun registrarVenda(venda: Venda) {
        viewModelScope.launch {
            repository.registrarVenda(venda)
            carregarVendas()
        }
    }

    fun deletarVenda(id: String) {
        viewModelScope.launch {
            repository.deletarVenda(id)
            carregarVendas()
        }
    }
}
