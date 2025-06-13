package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Pedido
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.Venda
import com.example.sweetjoygeladinhos.repository.EstoqueRepository
import com.example.sweetjoygeladinhos.repository.PedidoRepository
import com.example.sweetjoygeladinhos.repository.ProdutoRepository
import com.example.sweetjoygeladinhos.repository.VendaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PedidosViewModel : ViewModel() {

    private val pedidoRepository = PedidoRepository()
    private val produtoRepository = ProdutoRepository()
    private val estoqueRepository = EstoqueRepository()
    private val vendaRepository = VendaRepository()

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _vendas = MutableStateFlow<List<Venda>>(emptyList())
    val vendas: StateFlow<List<Venda>> = _vendas

    private val _produtosSelecionados = MutableStateFlow<Map<Produto, Int>>(emptyMap())
    val produtosSelecionados: StateFlow<Map<Produto, Int>> = _produtosSelecionados

    init {
        carregarPedidos()
        carregarProdutos()
        carregarVendas()
    }

    fun carregarPedidos() {
        viewModelScope.launch {
            _pedidos.value = pedidoRepository.obterPedidos()
        }
    }

    fun carregarProdutos() {
        viewModelScope.launch {
            _produtos.value = produtoRepository.obterProdutos()
        }
    }

    fun carregarVendas() {
        viewModelScope.launch {
            _vendas.value = vendaRepository.obterVendas()
        }
    }

    fun adicionarProduto(produto: Produto) {
        val atual = _produtosSelecionados.value.toMutableMap()
        atual[produto] = (atual[produto] ?: 0) + 1
        _produtosSelecionados.value = atual
    }

    fun removerProduto(produto: Produto) {
        val atual = _produtosSelecionados.value.toMutableMap()
        val quantidade = (atual[produto] ?: 0) - 1
        if (quantidade > 0) {
            atual[produto] = quantidade
        } else {
            atual.remove(produto)
        }
        _produtosSelecionados.value = atual
    }

    fun limparPedido() {
        _produtosSelecionados.value = emptyMap()
    }

    fun salvarPedido() {
        viewModelScope.launch {
            val produtosMap = _produtosSelecionados.value
            if (produtosMap.isNotEmpty()) {
                // calcula total do pedido
                val total = produtosMap.entries.sumOf { it.key.preco * it.value }

                // converte produtos para Map<String, Int> (id do produto e quantidade)
                val produtosParaPedido = produtosMap.map { it.key.id to it.value }.toMap()

                val pedido = Pedido(
                    produtos = produtosParaPedido,
                    total = total
                )
                pedidoRepository.adicionarPedido(pedido)

                val venda = Venda(
                    produtos = produtosParaPedido,
                    total = total
                )
                vendaRepository.registrarVenda(venda)

                // atualiza estoque no Firestore com quantidade negativa para retirar
                produtosMap.forEach { (produto, quantidade) ->
                    estoqueRepository.atualizarQuantidade(produto.id, -quantidade)
                }

                limparPedido()
                carregarPedidos()
                carregarVendas()
            }
        }
    }

    fun deletarPedido(id: String) {
        viewModelScope.launch {
            pedidoRepository.deletarPedido(id)
            carregarPedidos()
        }
    }
}
