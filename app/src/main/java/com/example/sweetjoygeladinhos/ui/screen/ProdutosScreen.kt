package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(navController: NavController) {
    val context = LocalContext.current
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }

    var produtos by remember { mutableStateOf(emptyList<Produto>()) }
    val coroutineScope = rememberCoroutineScope()

    // Estados para formulário e controle do diálogo
    var showDialog by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var produtoEmEdicao by remember { mutableStateOf<Produto?>(null) }

    // Diálogo de confirmação para deletar
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }

    // Carrega os produtos ao abrir a tela
    LaunchedEffect(Unit) {
        produtos = produtoDao.getAll()
    }

    fun carregarProdutos() {
        coroutineScope.launch {
            produtos = produtoDao.getAll()
        }
    }

    fun salvarProduto() {
        val precoDouble = preco.toDoubleOrNull() ?: return
        val produto = Produto(produtoEmEdicao?.produtoId ?: 0, nome, sabor, precoDouble)

        coroutineScope.launch {
            if (produtoEmEdicao == null) {
                // Novo produto
                produtoDao.insert(produto)
            } else {
                // Editar produto existente
                produtoDao.update(produto)
            }
            carregarProdutos()
            showDialog = false
            nome = ""; sabor = ""; preco = ""
            produtoEmEdicao = null
        }
    }
    fun deletarProduto() {
        produtoParaExcluir?.let {
            coroutineScope.launch {
                produtoDao.delete(it)
                carregarProdutos()
                showDeleteConfirm = false
                produtoParaExcluir = null
            }
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
            FloatingActionButton(onClick = {
                produtoEmEdicao = null
                nome = ""
                sabor = ""
                preco = ""
                showDialog = true
            }) {
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
            Text("Lista de Produtos", style = MaterialTheme.typography.titleLarge)

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
                            Text("Sabor: ${produto.sabor}")
                            Text("Preço: R$ %.2f".format(produto.preco))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    produtoEmEdicao = produto
                                    nome = produto.nome
                                    sabor = produto.sabor
                                    preco = produto.preco.toString()
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = {
                                    produtoParaExcluir = produto
                                    showDeleteConfirm = true
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para adicionar/editar
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
            title = { Text(if (produtoEmEdicao == null) "Novo Produto" else "Editar Produto") },
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

    // Confirmação de exclusão
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            confirmButton = {
                TextButton(onClick = { deletarProduto() }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar Exclusão") },
            text = {
                Text("Tem certeza que deseja excluir o produto \"${produtoParaExcluir?.nome}\"?")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProdutosScreen() {
    // Create a dummy NavController for the preview
    val navController = rememberNavController()
    ProdutosScreen(navController = navController)
}
