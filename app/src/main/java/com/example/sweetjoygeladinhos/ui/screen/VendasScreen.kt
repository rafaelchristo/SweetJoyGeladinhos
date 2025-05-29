package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Venda
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendasScreen(navController: NavController) {

    val softPink = Color(0xFFFFC1CC)
    val softRose = Color(0xFFFFD6E0)
    val darkRose = Color(0xFF8B1E3F)
    val softRed = Color(0xFFFFB3B3)

    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val vendaDao = remember { SweetJoyApp.database.vendaDao() }
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }
    val coroutineScope = rememberCoroutineScope()

    val estoqueList by estoqueDao.getAll().collectAsState(initial = emptyList())
    var vendasRegistradas by remember { mutableStateOf<List<Pair<Venda, String>>>(emptyList()) }

    var produtoSelecionado by remember { mutableStateOf<EstoqueItemComProduto?>(null) }
    var quantidadeVenda by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var editandoVenda by remember { mutableStateOf<Venda?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var vendaParaExcluir by remember { mutableStateOf<Venda?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    fun carregarVendas() {
        coroutineScope.launch {
            val vendas = vendaDao.getAll()
            val vendasComNomes = vendas.mapNotNull { venda ->
                val produto = produtoDao.getById(venda.produtoId)
                produto?.let { venda to it.nome }
            }
            vendasRegistradas = vendasComNomes
        }
    }

    LaunchedEffect(Unit) {
        carregarVendas()
    }

    fun mostrarSnackbar(mensagem: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(mensagem)
        }
    }

    fun registrarOuAtualizarVenda() {
        val quantidadeInt = quantidadeVenda.toIntOrNull()
        val estoqueItem = produtoSelecionado?.item

        if (estoqueItem == null) {
            mostrarSnackbar("Selecione um produto.")
            return
        }

        if (quantidadeInt == null || quantidadeInt <= 0) {
            mostrarSnackbar("Informe uma quantidade válida.")
            return
        }

        if (quantidadeInt > estoqueItem.quantidade && editandoVenda == null) {
            mostrarSnackbar("Quantidade maior que o estoque disponível.")
            return
        }

        coroutineScope.launch {
            if (editandoVenda != null) {
                val vendaAtualizada = editandoVenda!!.copy(
                    produtoId = estoqueItem.produtoId,
                    quantidade = quantidadeInt
                )
                vendaDao.update(vendaAtualizada)
                mostrarSnackbar("Venda atualizada com sucesso!")
                editandoVenda = null
            } else {
                estoqueDao.updateEstoqueItem(estoqueItem.copy(quantidade = estoqueItem.quantidade - quantidadeInt))

                val data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                val novaVenda = Venda(
                    produtoId = estoqueItem.produtoId,
                    quantidade = quantidadeInt,
                    dataVenda = data
                )
                vendaDao.insert(novaVenda)
                mostrarSnackbar("Venda registrada com sucesso!")
            }

            carregarVendas()
            produtoSelecionado = null
            quantidadeVenda = ""
        }
    }

    fun confirmarExclusao(venda: Venda) {
        vendaParaExcluir = venda
        showDeleteDialog = true
    }

    fun excluirVenda() {
        vendaParaExcluir?.let { venda ->
            coroutineScope.launch {
                vendaDao.delete(venda)
                mostrarSnackbar("Venda excluída com sucesso.")
                carregarVendas()
            }
        }
        showDeleteDialog = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vendas", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = softPink,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = if (editandoVenda != null) "Editar Venda" else "Registrar Venda",
                    style = MaterialTheme.typography.titleMedium,
                    color = darkRose
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = produtoSelecionado?.produto?.nome ?: "",
                        onValueChange = {},
                        label = { Text("Produto") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { registrarOuAtualizarVenda() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = softPink,
                        contentColor = Color.White
                    )
                ) {
                    Text(if (editandoVenda != null) "Atualizar Venda" else "Registrar Venda")
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Estoque Atual", style = MaterialTheme.typography.titleMedium, color = darkRose)
            }

            items(estoqueList) { item ->
                val isLowStock = item.item.quantidade <= 10
                val textColor = if (isLowStock) softRed else Color.Unspecified

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = softRose
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = item.produto.sabor, color = darkRose)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isLowStock) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Baixo estoque",
                                        tint = softRed,
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

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Vendas Registradas", style = MaterialTheme.typography.titleMedium, color = darkRose)
            }

            items(vendasRegistradas) { (venda, nomeProduto) ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = softRose
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Produto: $nomeProduto", color = darkRose)
                        Text("Quantidade: ${venda.quantidade}")
                        Text("Data da Venda: ${venda.dataVenda}")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    produtoSelecionado = estoqueList.find { it.item.produtoId == venda.produtoId }
                                    quantidadeVenda = venda.quantidade.toString()
                                    editandoVenda = venda
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = darkRose
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Editar")
                            }

                            OutlinedButton(
                                onClick = { confirmarExclusao(venda) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Excluir")
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
                TextButton(onClick = { excluirVenda() }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir esta venda?") }
        )
    }
}
