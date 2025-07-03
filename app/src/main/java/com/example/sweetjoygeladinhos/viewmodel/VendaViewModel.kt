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

    fun registrarVenda(venda: Venda, onEstoqueInsuficiente: (produtoId: String) -> Unit = {}) {
        viewModelScope.launch {
            val estoqueAtual = estoqueRepository.obterTodosComProduto()

            // Verifica se todos os produtos têm estoque suficiente
            val algumInsuficiente = venda.produtos.any { (produtoId, quantidade) ->
                val atual = estoqueAtual.find { it.item.produtoId == produtoId }?.item?.quantidade ?: 0
                quantidade > atual
            }

            if (algumInsuficiente) {
                venda.produtos.forEach { (produtoId, quantidade) ->
                    val atual = estoqueAtual.find { it.item.produtoId == produtoId }?.item?.quantidade ?: 0
                    if (quantidade > atual) {
                        onEstoqueInsuficiente(produtoId)
                    }
                }
                return@launch
            }

            // ✅ Subtrai de forma segura
            venda.produtos.forEach { (produtoId, quantidade) ->
                estoqueRepository.subtrairQuantidade(produtoId, quantidade)
            }

            // Registra a venda
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
