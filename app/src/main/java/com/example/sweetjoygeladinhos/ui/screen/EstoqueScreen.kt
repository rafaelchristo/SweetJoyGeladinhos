package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel

@Composable
fun EstoqueScreen(
    navController: NavHostController,
    viewModel: EstoqueViewModel = viewModel()
) {
    val estoque by viewModel.estoque.collectAsState()
    val erro by viewModel.erro.collectAsState()

    var produtoId by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }

    var itemEditando by remember { mutableStateOf<EstoqueItem?>(null) }
    var itemExcluindo by remember { mutableStateOf<EstoqueItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gerenciar Estoque", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = produtoId,
            onValueChange = { produtoId = it },
            label = { Text("ID do Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = sabor,
            onValueChange = { sabor = it },
            label = { Text("Sabor") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (produtoId.isNotBlank() && quantidade.isNotBlank()) {
                    val item = EstoqueItem(
                        produtoId = produtoId,
                        quantidade = quantidade.toIntOrNull() ?: 0,
                        sabor = sabor
                    )
                    viewModel.adicionarEstoqueItem(item)
                    produtoId = ""
                    quantidade = ""
                    sabor = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Adicionar ao Estoque")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Itens no Estoque", style = MaterialTheme.typography.titleMedium)

        if (erro != null) {
            Text("Erro: $erro", color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(estoque) { item ->
                EstoqueItemCard(
                    item = item,
                    onDelete = { itemExcluindo = item },
                    onEdit = { itemEditando = it }
                )
            }
        }
    }

    itemEditando?.let { item ->
        EditarEstoqueDialog(
            item = item,
            onDismiss = { itemEditando = null },
            onConfirm = { atualizado ->
                viewModel.atualizarEstoqueItem(atualizado)
                itemEditando = null
            }
        )
    }

    itemExcluindo?.let { item ->
        ConfirmarExclusaoDialog(
            item = item,
            onDismiss = { itemExcluindo = null },
            onConfirm = {
                viewModel.excluirEstoqueItem(item.produtoId)
                itemExcluindo = null
            }
        )
    }
}

@Composable
fun EstoqueItemCard(
    item: EstoqueItem,
    onDelete: () -> Unit,
    onEdit: (EstoqueItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Produto ID: ${item.produtoId}")
                Text("Quantidade: ${item.quantidade}")
                Text("Sabor: ${item.sabor}")
            }
            Row {
                IconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}

@Composable
fun EditarEstoqueDialog(
    item: EstoqueItem,
    onDismiss: () -> Unit,
    onConfirm: (EstoqueItem) -> Unit
) {
    var quantidade by remember { mutableStateOf(item.quantidade.toString()) }
    var sabor by remember { mutableStateOf(item.sabor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val atualizado = item.copy(
                    quantidade = quantidade.toIntOrNull() ?: item.quantidade,
                    sabor = sabor
                )
                onConfirm(atualizado)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Editar Estoque") },
        text = {
            Column {
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Quantidade") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = sabor,
                    onValueChange = { sabor = it },
                    label = { Text("Sabor") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun ConfirmarExclusaoDialog(
    item: EstoqueItem,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Excluir")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Confirmar Exclusão") },
        text = { Text("Tem certeza que deseja excluir o item de produto ID '${item.produtoId}'?") }
    )
}
