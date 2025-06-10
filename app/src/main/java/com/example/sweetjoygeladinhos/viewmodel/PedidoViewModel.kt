package com.example.sweetjoygeladinhos.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.data.EstoqueDao
import com.example.sweetjoygeladinhos.data.PedidoDao
import com.example.sweetjoygeladinhos.data.ProdutoDao
import com.example.sweetjoygeladinhos.data.VendaDao
import com.example.sweetjoygeladinhos.model.Pedido
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.Venda
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PedidosViewModel(
    private val produtoDao: ProdutoDao,
    private val pedidoDao: PedidoDao,
    private val estoqueDao: EstoqueDao,
    private val vendaDao: VendaDao
) : ViewModel() {

    private val _produtos = mutableStateOf<List<Produto>>(emptyList())
    val produtos: State<List<Produto>> get() = _produtos

    private val _produtosSelecionados = mutableStateOf<Map<Produto, Int>>(emptyMap())
    val produtosSelecionados: State<Map<Produto, Int>> get() = _produtosSelecionados

    init {
        carregarProdutos()
    }

    private fun carregarProdutos() {
        viewModelScope.launch {
            produtoDao.getAll().collect { lista ->
                _produtos.value = lista
            }
        }
    }

    fun adicionarProduto(produto: Produto) {
        val atual = _produtosSelecionados.value.toMutableMap()
        atual[produto] = (atual[produto] ?: 0) + 1
        _produtosSelecionados.value = atual
    }

    fun removerProduto(produto: Produto) {
        val atual = _produtosSelecionados.value.toMutableMap()
        val quantidadeAtual = atual[produto] ?: return
        if (quantidadeAtual > 1) {
            atual[produto] = quantidadeAtual - 1
        } else {
            atual.remove(produto)
        }
        _produtosSelecionados.value = atual
    }

    fun gerarResumoPedido(): String {
        return _produtosSelecionados.value.entries.joinToString("\n") { (produto, quantidade) ->
            "${produto.sabor} - Quantidade: $quantidade"
        }
    }

    fun salvarPedido() {
        val resumo = gerarResumoPedido()
        val total = _produtosSelecionados.value.entries.sumOf { it.key.preco * it.value.toDouble() }

        if (_produtosSelecionados.value.isNotEmpty()) {
            viewModelScope.launch {
                pedidoDao.inserir(
                    Pedido(
                        detalhes = resumo,
                        total = total
                    )
                )

                val dataVenda = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                _produtosSelecionados.value.forEach { (produto, quantidade) ->
                    vendaDao.insert(Venda(produtoId = produto.produtoId, quantidade = quantidade, dataVenda = dataVenda))

                    estoqueDao.getByProdutoId(produto.produtoId)?.let {
                        val novaQuantidade = (it.quantidade - quantidade).coerceAtLeast(0)
                        estoqueDao.updateEstoqueItem(it.copy(quantidade = novaQuantidade))
                    }
                }

                limparPedido()
            }
        }
    }

    fun limparPedido() {
        _produtosSelecionados.value = emptyMap()
    }
}
