package com.example.sweetjoygeladinhos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.repository.EstoqueRepository
import com.example.sweetjoygeladinhos.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstoqueViewModel : ViewModel() {

    private val produtoRepository = ProdutoRepository()
    private val estoqueRepository = EstoqueRepository()

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _estoque = MutableStateFlow<List<EstoqueItemComProduto>>(emptyList())
    val estoque: StateFlow<List<EstoqueItemComProduto>> = _estoque

    private val _carregandoProdutos = MutableStateFlow(true)
    val carregandoProdutos: StateFlow<Boolean> = _carregandoProdutos

    init {
        carregarProdutos()
        carregarEstoque()
    }

    private fun carregarProdutos() = viewModelScope.launch {
        try {
            _carregandoProdutos.value = true
            _produtos.value = produtoRepository.obterProdutos()
        } catch (e: Exception) {
            Log.e("EstoqueVM", "Erro ao carregar produtos", e)
        } finally {
            _carregandoProdutos.value = false
        }
    }

    private fun carregarEstoque() = viewModelScope.launch {
        try {
            val estoqueAtual = estoqueRepository.obterTodosComProduto()

            if (estoqueAtual.isEmpty()) {
                val produtosExistentes = produtoRepository.obterProdutos()
                produtosExistentes.forEach { produto ->
                    val novoItem = EstoqueItem(
                        produtoId = produto.id,
                        quantidade = 0
                    )
                    estoqueRepository.adicionarOuAtualizarItem(novoItem)
                }
            }

            _estoque.value = estoqueRepository.obterTodosComProduto()
        } catch (e: Exception) {
            Log.e("EstoqueVM", "Erro ao carregar estoque", e)
        }
    }

    fun salvarEstoque(item: EstoqueItem) = viewModelScope.launch {
        try {
            estoqueRepository.adicionarOuAtualizarItem(item)
            carregarEstoque()
        } catch (e: Exception) {
            Log.e("EstoqueVM", "Erro ao salvar estoque", e)
        }
    }

    fun excluirEstoque(item: EstoqueItem) = viewModelScope.launch {
        try {
            estoqueRepository.deletarItem(item.produtoId)
            carregarEstoque()
        } catch (e: Exception) {
            Log.e("EstoqueVM", "Erro ao excluir estoque", e)
        }
    }
}
