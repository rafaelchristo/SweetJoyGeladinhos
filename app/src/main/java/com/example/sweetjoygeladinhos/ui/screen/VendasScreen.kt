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
import androidx.room.PrimaryKey
import kotlin.String
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendasScreen() {
    // Lista de itens no estoque com produtos
    var estoqueList by remember {
        mutableStateOf(
            listOf(
                EstoqueItemComProduto(
                    estoqueItem = EstoqueItem(1, produtoId = 1L, quantidade = 30),
                    produto = Produto(1, "Morango", "Chocolate Branco", 2.5)
                )
            )
        )
    }

    // Estado para seleção de produto e quantidade
    var produtoSelecionado by remember { mutableStateOf<EstoqueItemComProduto?>(null) }
    var quantidadeVenda by remember { mutableStateOf("") }
    var vendasRegistradas by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    // Função para registrar venda
    fun registrarVenda() {
        val quantidadeInt = quantidadeVenda.toIntOrNull() ?: 0
        val estoqueItem = produtoSelecionado?.estoqueItem

        if (estoqueItem != null && quantidadeInt > 0 && estoqueItem.quantidade >= quantidadeInt) {
            val atualizado = estoqueItem.copy(quantidade = estoqueItem.quantidade - quantidadeInt)

            estoqueList = estoqueList.map {
                if (it.estoqueItem.estoqueId == estoqueItem.estoqueId) {
                    it.copy(estoqueItem = atualizado)
                } else it
            }

            vendasRegistradas = vendasRegistradas + Pair(produtoSelecionado!!.produto.nome, quantidadeInt)

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
                            text = { Text(item.produto.sabor) },
                            onClick = {
                                produtoSelecionado = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantidadeVenda,
                onValueChange = { quantidadeVenda = it },
                label = { Text("Quantidade de Venda") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { registrarVenda() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Venda")
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                            Text("Produto: ${venda.first}")
                            Text("Quantidade: ${venda.second}")
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