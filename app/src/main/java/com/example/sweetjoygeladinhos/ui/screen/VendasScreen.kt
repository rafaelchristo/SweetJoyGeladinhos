package com.example.sweetjoygeladinhos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sweetjoygeladinhos.model.Venda
import com.example.sweetjoygeladinhos.viewmodel.VendaViewModel
import androidx.compose.foundation.lazy.items //
import java.util.*

@Composable
fun VendasScreen(navController: NavHostController, vendaViewModel: VendaViewModel = viewModel()) {
    val vendas by vendaViewModel.vendas.collectAsState()
    val erro by vendaViewModel.erro.collectAsState()

    var produtoId by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var dataVenda by remember { mutableStateOf("") }
    var editarVenda by remember { mutableStateOf<Venda?>(null) }

    LaunchedEffect(Unit) {
        vendaViewModel.carregarVendas()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Cadastro de Vendas", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = produtoId,
            onValueChange = { produtoId = it },
            label = { Text("Produto ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = quantidade,
            onValueChange = { quantidade = it.filter { c -> c.isDigit() } },
            label = { Text("Quantidade") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dataVenda,
            onValueChange = { dataVenda = it },
            label = { Text("Data Venda (dd/MM/yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val q = quantidade.toIntOrNull() ?: 0
                if (produtoId.isBlank() || q <= 0 || dataVenda.isBlank()) {
                    // Você pode mostrar um snackbar ou mensagem de erro
                    return@Button
                }

                val novaVenda = editarVenda?.copy(
                    produtoId = produtoId,
                    quantidade = q,
                    dataVenda = dataVenda
                ) ?: Venda(
                    produtoId = produtoId,
                    quantidade = q,
                    dataVenda = dataVenda
                )

                if (editarVenda == null) {
                    vendaViewModel.adicionarVenda(novaVenda)
                } else {
                    vendaViewModel.atualizarVenda(novaVenda)
                }

                // limpar campos
                produtoId = ""
                quantidade = ""
                dataVenda = ""
                editarVenda = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editarVenda == null) "Adicionar Venda" else "Atualizar Venda")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Vendas Cadastradas", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(8.dp))

        if (erro != null) {
            Text(text = "Erro: $erro", color = MaterialTheme.colors.error)
        }

        LazyColumn {
            items(vendas) { venda ->
                VendaItem(venda,
                    onEditar = {
                        produtoId = it.produtoId
                        quantidade = it.quantidade.toString()
                        dataVenda = it.dataVenda
                        editarVenda = it
                    },
                    onExcluir = {
                        vendaViewModel.deletarVenda(it.id)
                    }
                )
            }
        }
    }
}

@Composable
fun VendaItem(venda: Venda, onEditar: (Venda) -> Unit, onExcluir: (Venda) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Produto ID: ${venda.produtoId}")
                Text("Quantidade: ${venda.quantidade}")
                Text("Data: ${venda.dataVenda}")
            }
            Row {
                IconButton(onClick = { onEditar(venda) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { onExcluir(venda) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}
