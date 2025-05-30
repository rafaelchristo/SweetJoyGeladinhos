package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // ✅ Import necessário
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.viewmodel.ProdutoViewModel
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(
    navController: NavHostController,
    viewModel: ProdutoViewModel
) {
    val produtos by viewModel.produtos.collectAsState()
    val erro by viewModel.erro.collectAsState()

    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var editandoProduto by remember { mutableStateOf<Produto?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.carregarProdutos()
    }

    LaunchedEffect(erro) {
        erro?.let {
            snackbarHostState.showSnackbar("Erro: $it")
        }
    }

    fun limparCampos() {
        nome = ""
        sabor = ""
        preco = ""
        editandoProduto = null
    }

    fun validarEEnviar() {
        val precoDouble = preco.toDoubleOrNull()
        if (nome.isBlank() || sabor.isBlank() || precoDouble == null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Preencha todos os campos corretamente.")
            }
            return
        }

        val produto = Produto(
            produtoId = editandoProduto?.produtoId ?: "",
            nome = nome,
            sabor = sabor,
            preco = precoDouble
        )

        if (editandoProduto == null) {
            viewModel.adicionarProduto(produto)
        } else {
            viewModel.atualizarProduto(produto)
        }
        limparCampos()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Produtos") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { validarEEnviar() }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (produtos.isEmpty()) {
                Text("Nenhum produto cadastrado.")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(produtos) { produto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = produto.nome, style = MaterialTheme.typography.titleMedium)
                                Text(text = "Sabor: ${produto.sabor}")
                                Text(text = "Preço: R$ ${produto.preco}")
                            }
                            Row {
                                IconButton(onClick = {
                                    editandoProduto = produto
                                    nome = produto.nome
                                    sabor = produto.sabor
                                    preco = produto.preco.toString()
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = {
                                    viewModel.deletarProduto(produto.produtoId)
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
}
