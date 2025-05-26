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
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(navController: NavController) {
    val context = LocalContext.current
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }

    val coroutineScope = rememberCoroutineScope()

    // Observe flows diretamente
    val produtos by produtoDao.getAll().collectAsState(initial = emptyList())
    val estoque by estoqueDao.getAll().collectAsState(initial = emptyList())

    var selectedProduto by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Para edição: armazenar produtoId do item em edição
    var editandoProdutoId by remember { mutableStateOf<Long?>(null) }

    // Diálogo de exclusão
    var showDeleteDialog by remember { mutableStateOf(false) }
    var estoqueParaExcluir by remember { mutableStateOf<EstoqueItemComProduto?>(null) }

    fun salvarEstoque() {
        val produto = selectedProduto ?: return
        val quantidadeInt = quantidade.toIntOrNull() ?: return

        coroutineScope.launch {
            if (editandoProdutoId != null) {
                estoqueDao.updateEstoqueItem(
                    EstoqueItem(
                        produtoId = editandoProdutoId!!,
                        quantidade = quantidadeInt
                    )
                )
                editandoProdutoId = null
            } else {
                estoqueDao.insert(produto.produtoId, quantidadeInt)
            }
            quantidade = ""
            selectedProduto = null
        }
    }

    fun confirmarExclusao(item: EstoqueItemComProduto) {
        estoqueParaExcluir = item
        showDeleteDialog = true
    }

    fun excluirEstoque() {
        estoqueParaExcluir?.let { item ->
            coroutineScope.launch {
                estoqueDao.deleteEstoqueItem(item.item)
            }
        }
        showDeleteDialog = false
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
            Text(
                text = if (editandoProdutoId != null) "Editar Estoque" else "Adicionar Estoque",
                style = MaterialTheme.typography.titleMedium
            )

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
                Text(if (editandoProdutoId != null) "Atualizar Estoque" else "Salvar Estoque")
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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        selectedProduto = item.produto
                                        quantidade = item.item.quantidade.toString()
                                        editandoProdutoId = item.produto.produtoId
                                    }
                                ) {
                                    Text("Editar")
                                }
                                OutlinedButton(
                                    onClick = { confirmarExclusao(item) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = { excluirEstoque() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir este item do estoque?") }
        )
    }
}
