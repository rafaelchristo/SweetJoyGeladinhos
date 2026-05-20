package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarEventoScreen(
    navController: NavController,
    viewModel: EstoqueViewModel = viewModel()
) {
    val produtos by viewModel.produtos.collectAsState()
    val carregandoProdutos by viewModel.carregandoProdutos.collectAsState()

    var nomeEvento by remember { mutableStateOf("") }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Lista de itens adicionados ao evento
    var itensEvento by remember { mutableStateOf(listOf<Pair<Produto, Int>>()) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Criar Novo Evento") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = nomeEvento,
                onValueChange = { nomeEvento = it },
                label = { Text("Nome do Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Adicionar Produtos ao Evento", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            if (carregandoProdutos) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = produtoSelecionado?.nome ?: "Selecione um produto",
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
                                text = { Text(produto.nome) },
                                onClick = {
                                    produtoSelecionado = produto
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = quantidade,
                    onValueChange = { if (it.all { char -> char.isDigit() }) quantidade = it },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val prod = produtoSelecionado
                        val qtd = quantidade.toIntOrNull() ?: 0
                        if (prod != null && qtd > 0) {
                            itensEvento = itensEvento + (prod to qtd)
                            quantidade = ""
                            produtoSelecionado = null
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Produtos no Evento", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(itensEvento) { (produto, qtd) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(produto.nome, style = MaterialTheme.typography.bodyLarge)
                                Text("Quantidade: $qtd", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = {
                                itensEvento = itensEvento.filterNot { it.first.id == produto.id && it.second == qtd }
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remover",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nomeEvento.isNotBlank() && itensEvento.isNotEmpty()) {
                        // TODO: Salvar o evento no banco futuramente
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nomeEvento.isNotBlank() && itensEvento.isNotEmpty()
            ) {
                Text("Confirmar Evento")
            }
        }
    }
}
