package com.example.sweetjoygeladinhos.ui.screens

import SweetJoyGeladinhosTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel
import com.example.sweetjoygeladinhos.viewmodel.VendaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VendasScreen(
    vendaViewModel: VendaViewModel,
    estoqueViewModel: EstoqueViewModel = viewModel()
) {
    SweetJoyGeladinhosTheme {
        val vendas by vendaViewModel.vendas.collectAsState()
        val estoqueList by estoqueViewModel.estoque.collectAsState()
        val carregando by estoqueViewModel.carregandoProdutos.collectAsState()

        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        var itensVenda by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

        fun adicionarOuAtualizarProduto(produtoId: String, quantidade: Int) {
            val estoqueDisponivel = estoqueList.find { it.item.produtoId == produtoId }?.item?.quantidade ?: 0
            if (quantidade <= 0) {
                itensVenda = itensVenda.toMutableMap().apply { remove(produtoId) }
            } else if (quantidade <= estoqueDisponivel) {
                itensVenda = itensVenda.toMutableMap().apply { put(produtoId, quantidade) }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Quantidade disponível no estoque: $estoqueDisponivel")
                }
            }
        }

        fun calcularTotal(): Double {
            return itensVenda.entries.sumOf { (produtoId, qtd) ->
                val preco = estoqueList.find { it.item.produtoId == produtoId }?.produto?.preco ?: 0.0
                preco * qtd
            }
        }

        fun registrarVenda() {
            if (itensVenda.isEmpty()) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Selecione ao menos um produto com quantidade")
                }
                return
            }

            val produtoComEstoqueInsuficiente = itensVenda.any { (produtoId, qtd) ->
                val estoqueDisponivel = estoqueList.find { it.item.produtoId == produtoId }?.item?.quantidade ?: 0
                qtd > estoqueDisponivel
            }

            if (produtoComEstoqueInsuficiente) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Quantidade de algum produto ultrapassa o estoque disponível.")
                }
                return
            }

            vendaViewModel.registrarVenda(
                com.example.sweetjoygeladinhos.model.Venda(
                    produtos = itensVenda,
                    total = calcularTotal(),
                    dataVenda = System.currentTimeMillis()
                )
            )
            itensVenda = emptyMap()
            estoqueViewModel.carregarEstoque()
        }

        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Registrar Venda", style = MaterialTheme.typography.headlineSmall)
                    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                }

                if (carregando) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(estoqueList) { item ->
                        val produtoId = item.item.produtoId
                        val nome = item.produto.nome
                        val quantidadeSelecionada = itensVenda[produtoId] ?: 0
                        val estoqueDisponivel = item.item.quantidade

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = nome,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                val novaQtd = (quantidadeSelecionada - 1).coerceAtLeast(0)
                                adicionarOuAtualizarProduto(produtoId, novaQtd)
                            }) {
                                Icon(Icons.Default.Remove, contentDescription = "Diminuir")
                            }
                            Text(
                                text = quantidadeSelecionada.toString(),
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center
                            )
                            IconButton(onClick = {
                                val novaQtd = quantidadeSelecionada + 1
                                adicionarOuAtualizarProduto(produtoId, novaQtd)
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Aumentar")
                            }
                        }
                    }

                    item {
                        Text("Total: R$ %.2f".format(calcularTotal()), style = MaterialTheme.typography.titleMedium)
                        Button(
                            onClick = { registrarVenda() },
                            enabled = itensVenda.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Registrar Venda")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Vendas Registradas", style = MaterialTheme.typography.headlineSmall)
                    Divider(thickness = 2.dp, color = MaterialTheme.colorScheme.secondary)
                }

                items(vendaViewModel.vendas.value.sortedByDescending { it.dataVenda }) { venda ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Data: ${SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(venda.dataVenda))}")
                            Text("Total: R$ %.2f".format(venda.total))
                            Spacer(Modifier.height(4.dp))
                            venda.produtos.forEach { (produtoId, qtd) ->
                                val produto = estoqueList.find { it.item.produtoId == produtoId }?.produto
                                val nome = produto?.nome ?: "Produto desconhecido"
                                Text("$nome: $qtd")
                            }
                            Spacer(Modifier.height(8.dp))
                            Row {
                                OutlinedButton(onClick = {
                                    // Implementar edição se desejar
                                }) {
                                    Text("Editar")
                                }
                                Spacer(Modifier.width(8.dp))
                                OutlinedButton(onClick = {
                                    vendaViewModel.deletarVenda(venda.id)
                                    estoqueViewModel.carregarEstoque()
                                }) {
                                    Text("Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
