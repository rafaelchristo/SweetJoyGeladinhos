package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Venda
import com.example.sweetjoygeladinhos.repository.EstoqueRepository
import com.example.sweetjoygeladinhos.repository.VendaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VendaViewModel : ViewModel() {
    private val vendaRepository = VendaRepository()
    private val estoqueRepository = EstoqueRepository()

    private val _vendas = MutableStateFlow<List<Venda>>(emptyList())
    val vendas: StateFlow<List<Venda>> = _vendas

    init {
        carregarVendas()
    }

    fun carregarVendas() {
        viewModelScope.launch {
            _vendas.value = vendaRepository.obterVendas()
        }
    }

    fun registrarVenda(venda: Venda) {
        viewModelScope.launch {
            // Atualiza o estoque subtraindo as quantidades da venda
            venda.produtos.forEach { (produtoId, quantidade) ->
                estoqueRepository.atualizarQuantidade(produtoId, -quantidade)
            }
            vendaRepository.registrarVenda(venda)
            carregarVendas()
        }
    }

    fun deletarVenda(id: String) {
        viewModelScope.launch {
            vendaRepository.deletarVenda(id)
            carregarVendas()
        }
    }
}
