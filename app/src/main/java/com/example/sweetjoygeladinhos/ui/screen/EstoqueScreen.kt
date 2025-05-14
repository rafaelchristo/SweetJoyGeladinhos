package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.EstoqueItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen() {
    val context = LocalContext.current
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }

    val coroutineScope = rememberCoroutineScope()

    var produtos by remember { mutableStateOf(emptyList<com.example.sweetjoygeladinhos.model.Produto>()) }
    var estoque by remember { mutableStateOf(emptyList<EstoqueItemComProduto>()) }

    var selectedProduto by remember { mutableStateOf<com.example.sweetjoygeladinhos.model.Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        produtos = produtoDao.getAll()
        estoque = estoqueDao.getAll()
    }

    fun salvarEstoque() {
        val produto = selectedProduto ?: return
        val quantidadeInt = quantidade.toIntOrNull() ?: return

        coroutineScope.launch {
            estoqueDao.insert(produto.produtoId, quantidadeInt)
            estoque = estoqueDao.getAll()
            quantidade = ""
            selectedProduto = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Controle de Estoque", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Selecionar Produto", style = MaterialTheme.typography.titleMedium)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedProduto?.nome ?: "",
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    produtos.forEach { produto ->
                        DropdownMenuItem(
                            text = { Text("${produto.nome} (${produto.sabor})") },
                            onClick = {
                                selectedProduto = produto
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { salvarEstoque() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Estoque")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Itens no Estoque", style = MaterialTheme.typography.titleLarge)

            LazyColumn {
                items(estoque) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Produto: ${item.produto.nome}", style = MaterialTheme.typography.titleMedium)
                            Text("Sabor: ${item.produto.sabor}")
                            Text("Quantidade: ${item.item.quantidade}")
                        }
                    }
                }
            }
            }

        }
    }

