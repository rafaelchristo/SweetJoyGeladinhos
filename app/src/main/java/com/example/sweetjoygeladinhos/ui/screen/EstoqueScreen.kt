package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(
    viewModel: EstoqueViewModel = viewModel()
) {
    val produtos by viewModel.produtos.collectAsState()
    val estoque by viewModel.estoque.collectAsState()
    val carregandoProdutos by viewModel.carregandoProdutos.collectAsState()

    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var produtoParaExcluir by remember { mutableStateOf<EstoqueItemComProduto?>(null) } // Para confirmação exclusão

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            Text("Estoque", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown para escolher produto ou loading
            when {
                carregandoProdutos -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                produtos.isNotEmpty() -> {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            readOnly = true,
                            value = produtoSelecionado?.nome?.ifEmpty { "(Selecione um produto)" }
                                ?: "(Selecione um produto)",
                            onValueChange = {},
                            label = { Text("Produto") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                                    text = { Text(produto.nome.ifEmpty { "(Sem nome)" }) },
                                    onClick = {
                                        produtoSelecionado = produto
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Nenhum produto encontrado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo quantidade (apenas números, teclado numérico)
            TextField(
                value = quantidade,
                onValueChange = { novoValor ->
                    if (novoValor.all { it.isDigit() }) quantidade = novoValor
                },
                label = { Text("Quantidade") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val qtd = quantidade.toIntOrNull() ?: 0
                    val produto = produtoSelecionado
                    if (produto != null && qtd > 0) {
                        viewModel.salvarEstoque(
                            EstoqueItem(
                                produtoId = produto.id,
                                quantidade = qtd
                            )
                        )
                        quantidade = ""
                        produtoSelecionado = null
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Estoque salvo com sucesso!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Estoque")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Itens em Estoque", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            // Grid 2 colunas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estoque) { itemComProduto ->
                    EstoqueItemCard(
                        itemComProduto = itemComProduto,
                        onAtualizar = { item ->
                            viewModel.salvarEstoque(item)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Estoque atualizado com sucesso!")
                            }
                        },
                        onExcluirSolicitado = { item ->
                            produtoParaExcluir = item
                        }
                    )
                }
            }
        }

        // Diálogo de confirmação para exclusão
        if (produtoParaExcluir != null) {
            AlertDialog(
                onDismissRequest = { produtoParaExcluir = null },
                title = { Text("Confirmação") },
                text = { Text("Deseja realmente excluir o produto \"${produtoParaExcluir!!.produto.nome}\" do estoque?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.excluirEstoque(produtoParaExcluir!!.item)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Item removido do estoque!")
                        }
                        produtoParaExcluir = null
                    }) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { produtoParaExcluir = null }) {
                        Text("Não")
                    }
                }
            )
        }
    }
}

@Composable
fun EstoqueItemCard(
    itemComProduto: EstoqueItemComProduto,
    onAtualizar: (EstoqueItem) -> Unit,
    onExcluirSolicitado: (EstoqueItemComProduto) -> Unit
) {
    var quantidadeEditada by remember { mutableStateOf(itemComProduto.item.quantidade.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = itemComProduto.produto.nome, style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = quantidadeEditada,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            quantidadeEditada = it
                        }
                    },
                    label = { Text("Un") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    val novaQtd = quantidadeEditada.toIntOrNull()
                    if (novaQtd != null) {
                        onAtualizar(
                            itemComProduto.item.copy(quantidade = novaQtd)
                        )
                    }
                }) {
                    Text("Atualizar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = {
                onExcluirSolicitado(itemComProduto)
            }) {
                Text("Excluir", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

