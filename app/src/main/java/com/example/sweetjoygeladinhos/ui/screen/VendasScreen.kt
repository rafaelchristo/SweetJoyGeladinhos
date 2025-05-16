package com.example.sweetjoygeladinhos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Venda
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendasScreen(navController: NavController) {
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val vendaDao = remember { SweetJoyApp.database.vendaDao() }
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }
    val coroutineScope = rememberCoroutineScope()

    var estoqueList by remember { mutableStateOf(emptyList<EstoqueItemComProduto>()) }
    var produtoSelecionado by remember { mutableStateOf<EstoqueItemComProduto?>(null) }
    var quantidadeVenda by remember { mutableStateOf("") }
    var vendasRegistradas by remember { mutableStateOf(emptyList<Pair<Venda, String>>()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        estoqueList = estoqueDao.getAll()
        val vendas = vendaDao.getAll()
        val vendasComNomes = vendas.mapNotNull { venda ->
            val produto = produtoDao.getById(venda.produtoId)
            produto?.let { venda to it.nome }
        }
        vendasRegistradas = vendasComNomes
    }

    fun registrarVenda() {
        val quantidadeInt = quantidadeVenda.toIntOrNull() ?: return
        val estoqueItem = produtoSelecionado?.item ?: return

        if (estoqueItem.quantidade >= quantidadeInt) {
            coroutineScope.launch {
                val novaQuantidade = estoqueItem.quantidade - quantidadeInt
                estoqueDao.updateEstoqueItem(estoqueItem.copy(quantidade = novaQuantidade))

                val data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val novaVenda = Venda(
                    produtoId = estoqueItem.produtoId,
                    quantidade = quantidadeInt,
                    dataVenda = data
                )
                vendaDao.insert(novaVenda)

                estoqueList = estoqueDao.getAll()
                val produto = produtoDao.getById(estoqueItem.produtoId)
                if (produto != null) {
                    vendasRegistradas = listOf(novaVenda to produto.nome) + vendasRegistradas
                }

                produtoSelecionado = null
                quantidadeVenda = ""
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vendas", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Selecione o Produto")
            Spacer(modifier = Modifier.height(8.dp))

            var fieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = produtoSelecionado?.produto?.nome ?: "",
                    onValueChange = {},
                    label = { Text("Produto") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            fieldSize = coordinates.size.toSize()
                        }
                        .clickable { expanded = true },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Abrir seleção",
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(with(LocalDensity.current) { fieldSize.width.toDp() })
                ) {
                    estoqueList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.produto.sabor) },
                            onClick = {
                                produtoSelecionado = item
                                expanded = false
                            }
                        )
                    }
                }
            }



            OutlinedTextField(
                value = quantidadeVenda,
                onValueChange = { quantidadeVenda = it },
                label = { Text("Quantidade de Venda") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { registrarVenda() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Venda")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Estoque Atual", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(estoqueList) { item ->
                    val isLowStock = item.item.quantidade <= 10
                    val textColor = if (isLowStock) Color.Red else Color.Unspecified

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = item.produto.sabor)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isLowStock) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Baixo estoque",
                                            tint = Color.Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = "Qtd: ${item.item.quantidade}",
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Vendas Registradas", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(vendasRegistradas) { (venda, nomeProduto) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Produto: $nomeProduto")
                            Text("Quantidade: ${venda.quantidade}")
                            Text("Data da Venda: ${venda.dataVenda}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewVendasScreen() {
    val navController = rememberNavController() // Create a NavController for the preview
    VendasScreen(navController = navController)
}
