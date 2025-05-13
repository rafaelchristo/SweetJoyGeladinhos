package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.EstoqueItem
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendasScreen() {
    // Estado para armazenar os produtos no estoque
    var estoqueList by remember {
        mutableStateOf(
            listOf(
                EstoqueItem(1, Produto(1, "Geladinho", "Morango com Chocolate Branco", 2.5), 30),
                EstoqueItem(2, Produto(2, "Geladinho", "Chocolate com Doce de Leite", 3.0), 20),
                EstoqueItem(3, Produto(3, "Geladinho", "Ninho com Nutella", 2.8), 25),
                EstoqueItem(4, Produto(4, "Geladinho", "Oreo", 2.8), 25),
                EstoqueItem(5, Produto(5, "Geladinho", "Mousse de Limão", 2.8), 25),
                EstoqueItem(6, Produto(6, "Geladinho", "Trufado de Maracujá", 2.8), 25),
                EstoqueItem(7, Produto(7, "Geladinho", "Paçoca", 2.8), 25),
                EstoqueItem(8, Produto(8, "Geladinho", "Pudim", 2.8), 25)
            )
        )
    }

    // Estado para os campos de venda
    var produtoSelecionado by remember { mutableStateOf<EstoqueItem?>(null) }
    var quantidadeVenda by remember { mutableStateOf("") }
    var vendasRegistradas by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    // Função para registrar uma venda
    fun registrarVenda() {
        val quantidadeInt = quantidadeVenda.toIntOrNull() ?: 0
        if (produtoSelecionado != null && quantidadeInt > 0 && produtoSelecionado!!.quantidade >= quantidadeInt) {
            // Atualiza o estoque
            val produtoAtualizado = produtoSelecionado!!.copy(
                quantidade = produtoSelecionado!!.quantidade - quantidadeInt
            )
            estoqueList = estoqueList.map {
                if (it.estoqueId == produtoSelecionado!!.estoqueId) produtoAtualizado else it
            }

            // Registra a venda
            vendasRegistradas = vendasRegistradas + Pair(produtoSelecionado!!.produto.nome, quantidadeInt)

            // Limpa os campos
            produtoSelecionado = null
            quantidadeVenda = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vendas") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Seleção de produto
            Text("Selecione o Produto")
            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = produtoSelecionado?.produto?.nome ?: "",
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    estoqueList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.produto.sabor) },  // <-- Aqui está a correção
                            onClick = {
                                produtoSelecionado = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Campo para a quantidade de venda
            OutlinedTextField(
                value = quantidadeVenda,
                onValueChange = { quantidadeVenda = it },
                label = { Text("Quantidade de Venda") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botão para registrar venda
            Button(
                onClick = { registrarVenda() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Venda")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exibição das vendas registradas
            Text(text = "Vendas Registradas", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(vendasRegistradas) { venda ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Produto: ${venda.first ?: "Desconhecido"}")  // Garantindo que não seja nulo
                            Text(text = "Quantidade: ${venda.second ?: 0}")  // Garantindo que não seja nulo
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewVendasScreen() {
    VendasScreen()
}