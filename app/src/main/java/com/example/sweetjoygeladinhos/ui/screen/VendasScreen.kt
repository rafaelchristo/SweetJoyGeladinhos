package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Venda
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendasScreen() {
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val vendaDao = remember { SweetJoyApp.database.vendaDao() }
    val coroutineScope = rememberCoroutineScope()

    var estoqueList by remember { mutableStateOf(emptyList<EstoqueItemComProduto>()) }
    var produtoSelecionado by remember { mutableStateOf<EstoqueItemComProduto?>(null) }
    var quantidadeVenda by remember { mutableStateOf("") }
    var vendasRegistradas by remember { mutableStateOf(emptyList<Venda>()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        estoqueList = estoqueDao.getAll()
        vendasRegistradas = vendaDao.getAll()
    }

    fun registrarVenda() {
        val quantidadeInt = quantidadeVenda.toIntOrNull() ?: return
        val estoqueItem = produtoSelecionado?.item ?: return

        if (estoqueItem.quantidade >= quantidadeInt) {
            coroutineScope.launch {
                estoqueDao.insert(estoqueItem.produtoId, -quantidadeInt)
                vendaDao.insert(
                    Venda(
                        produtoId = estoqueItem.produtoId,
                        quantidade = quantidadeInt
                    )
                )

                estoqueList = estoqueDao.getAll()
                vendasRegistradas = vendaDao.getAll()

                produtoSelecionado = null
                quantidadeVenda = ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vendas", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
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

            Box {
                OutlinedTextField(
                    value = produtoSelecionado?.produto?.nome ?: "",
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
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
                            Text("Produto ID: ${venda.produtoId}")
                            Text("Quantidade: ${venda.quantidade}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewVendasScreen() {
    VendasScreen()
}