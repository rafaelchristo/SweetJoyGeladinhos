package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen() {
    val context = LocalContext.current
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }

    var produtos by remember { mutableStateOf(emptyList<Produto>()) }
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        produtos = produtoDao.getAll()
    }

    fun salvarProduto() {
        val precoDouble = preco.toDoubleOrNull() ?: return
        val novoProduto = Produto(0, nome, sabor, precoDouble)
        coroutineScope.launch {
            produtoDao.insert(novoProduto)
            produtos = produtoDao.getAll()
            showDialog = false
            nome = ""; sabor = ""; preco = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Produtos", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Lista de Produtos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn {
                items(produtos) { produto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nome: ${produto.nome}", style = MaterialTheme.typography.titleMedium)
                            Text("Sabor: ${produto.sabor}", style = MaterialTheme.typography.bodyMedium)
                            Text("Preço: R$ %.2f".format(produto.preco), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { salvarProduto() }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Novo Produto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sabor,
                        onValueChange = { sabor = it },
                        label = { Text("Sabor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = preco,
                        onValueChange = { preco = it },
                        label = { Text("Preço") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProdutosScreen() {
    ProdutosScreen()
}