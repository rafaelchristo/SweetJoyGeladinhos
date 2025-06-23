package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.viewmodel.EstoqueViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Estoque", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown ou loading
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
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        readOnly = true,
                        value = produtoSelecionado?.nome?.ifEmpty { "(Selecione um produto)" } ?: "(Selecione um produto)",
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

        // Campo quantidade com teclado numérico
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
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Estoque")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Itens em Estoque", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(estoque) { itemComProduto ->
                EstoqueItemRow(
                    itemComProduto = itemComProduto,
                    onExcluir = { viewModel.excluirEstoque(it) }
                )
            }
        }
    }
}

@Composable
fun EstoqueItemRow(
    itemComProduto: EstoqueItemComProduto,
    onExcluir: (EstoqueItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = itemComProduto.produto.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = "Quantidade: ${itemComProduto.item.quantidade}")
            }
            TextButton(onClick = { onExcluir(itemComProduto.item) }) {
                Text("Excluir", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
