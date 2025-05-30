package com.example.sweetjoygeladinhos.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sweetjoygeladinhos.model.Receita
import com.example.sweetjoygeladinhos.viewmodel.ReceitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitaScreen(
    viewModel: ReceitaViewModel = viewModel()
) {
    val receitas by viewModel.receitas.collectAsState()
    val erro by viewModel.erro.collectAsState()

    var receitaEdit by remember { mutableStateOf<Receita?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.carregarReceitas()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Receitas") },
                actions = {
                    IconButton(onClick = {
                        receitaEdit = null
                        showDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (erro != null) {
                Text(
                    text = erro ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(receitas) { receita ->
                    ReceitaItem(
                        receita = receita,
                        onEdit = {
                            receitaEdit = it
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deletarReceita(it.id)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ReceitaDialog(
            receita = receitaEdit,
            onDismiss = { showDialog = false },
            onConfirm = { novaReceita ->
                if (novaReceita.id.isEmpty()) {
                    viewModel.adicionarReceita(novaReceita)
                } else {
                    viewModel.atualizarReceita(novaReceita)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ReceitaItem(receita: Receita, onEdit: (Receita) -> Unit, onDelete: (Receita) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit(receita) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = receita.nome, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Ingredientes: ${receita.ingredientes}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Modo: ${receita.modoPreparo}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Quantidade: ${receita.quantidade}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDelete(receita) }) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitaDialog(
    receita: Receita?,
    onDismiss: () -> Unit,
    onConfirm: (Receita) -> Unit
) {
    var nome by remember { mutableStateOf(TextFieldValue(receita?.nome ?: "")) }
    var ingredientes by remember { mutableStateOf(TextFieldValue(receita?.ingredientes ?: "")) }
    var modoPreparo by remember { mutableStateOf(TextFieldValue(receita?.modoPreparo ?: "")) }
    var quantidade by remember { mutableStateOf(TextFieldValue(receita?.quantidade?.toString() ?: "0")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (receita == null) "Adicionar Receita" else "Editar Receita") },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ingredientes,
                    onValueChange = { ingredientes = it },
                    label = { Text("Ingredientes") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = modoPreparo,
                    onValueChange = { modoPreparo = it },
                    label = { Text("Modo de Preparo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { newValue ->
                        val filteredText = newValue.text.filter { it.isDigit() }
                        quantidade = TextFieldValue(filteredText)
                    },
                    label = { Text("Quantidade") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val q = quantidade.text.toIntOrNull() ?: 0
                if (nome.text.isBlank()) return@TextButton

                val novaReceita = receita?.copy(
                    nome = nome.text,
                    ingredientes = ingredientes.text,
                    modoPreparo = modoPreparo.text,
                    quantidade = q
                ) ?: Receita(
                    id = "",
                    nome = nome.text,
                    ingredientes = ingredientes.text,
                    modoPreparo = modoPreparo.text,
                    quantidade = q
                )
                onConfirm(novaReceita)
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
