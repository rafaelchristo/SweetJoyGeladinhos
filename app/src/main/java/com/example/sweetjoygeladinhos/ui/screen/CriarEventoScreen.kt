package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.model.Evento
import com.example.sweetjoygeladinhos.model.EventoItem
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel
import com.example.sweetjoygeladinhos.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarEventoScreen(
    navController: NavController,
    eventoId: String? = null,
    estoqueViewModel: EstoqueViewModel = viewModel(),
    eventoViewModel: EventoViewModel = viewModel()
) {
    val produtos by estoqueViewModel.produtos.collectAsState()
    val carregandoProdutos by estoqueViewModel.carregandoProdutos.collectAsState()
    val eventos by eventoViewModel.eventos.collectAsState()

    var nomeEvento by remember { mutableStateOf("") }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Lista de itens adicionados ao evento
    var itensEvento by remember { mutableStateOf(listOf<Pair<Produto, Int>>()) }

    var itemParaEditar by remember { mutableStateOf<Pair<Produto, Int>?>(null) }
    var novaQuantidadeEditada by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    // Carregar dados se for edição
    LaunchedEffect(eventoId, eventos, produtos) {
        if (eventoId != null && produtos.isNotEmpty()) {
            val evento = eventos.find { it.id == eventoId }
            if (evento != null) {
                nomeEvento = evento.nome
                itensEvento = evento.itens.mapNotNull { item ->
                    val produto = produtos.find { it.id == item.produtoId }
                    if (produto != null) produto to item.quantidade else null
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (eventoId == null) "Criar Novo Evento" else "Editar Evento") }
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
                            Row {
                                IconButton(onClick = {
                                    itemParaEditar = produto to qtd
                                    novaQuantidadeEditada = qtd.toString()
                                }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar Quantidade",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nomeEvento.isNotBlank() && itensEvento.isNotEmpty()) {
                        val novoEvento = Evento(
                            id = eventoId ?: "",
                            nome = nomeEvento,
                            itens = itensEvento.map { (produto, qtd) ->
                                EventoItem(
                                    produtoId = produto.id,
                                    nomeProduto = produto.nome,
                                    quantidade = qtd
                                )
                            }
                        )

                        if (eventoId == null) {
                            eventoViewModel.criarEvento(novoEvento) {
                                navController.popBackStack()
                            }
                        } else {
                            eventoViewModel.atualizarEvento(novoEvento) {
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nomeEvento.isNotBlank() && itensEvento.isNotEmpty()
            ) {
                Text(if (eventoId == null) "Confirmar Evento" else "Salvar Alterações")
            }
        }

        if (itemParaEditar != null) {
            AlertDialog(
                onDismissRequest = { itemParaEditar = null },
                title = { Text("Editar Quantidade") },
                text = {
                    Column {
                        Text("Produto: ${itemParaEditar?.first?.nome}")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = novaQuantidadeEditada,
                            onValueChange = { if (it.all { char -> char.isDigit() }) novaQuantidadeEditada = it },
                            label = { Text("Nova Quantidade") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val novaQtd = novaQuantidadeEditada.toIntOrNull() ?: 0
                        if (novaQtd > 0) {
                            itensEvento = itensEvento.map {
                                if (it.first.id == itemParaEditar?.first?.id) {
                                    it.first to novaQtd
                                } else it
                            }
                        }
                        itemParaEditar = null
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { itemParaEditar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
