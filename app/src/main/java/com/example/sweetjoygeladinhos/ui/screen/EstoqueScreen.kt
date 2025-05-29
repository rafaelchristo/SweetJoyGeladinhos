package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen(navController: NavController) {

    val softPink = Color(0xFFFFC1CC)
    val softRose = Color(0xFFFFD6E0)
    val darkRose = Color(0xFF8B1E3F)
    val softRed = Color(0xFFFFB3B3)

    val context = LocalContext.current
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }
    val coroutineScope = rememberCoroutineScope()

    val produtos by produtoDao.getAll().collectAsState(initial = emptyList())
    val estoque by estoqueDao.getAll().collectAsState(initial = emptyList())

    var selectedProduto by remember { mutableStateOf<Produto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var editandoProdutoId by remember { mutableStateOf<Long?>(null) }
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
                    containerColor = softPink,
                    titleContentColor = Color.White
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
                style = MaterialTheme.typography.titleMedium,
                color = darkRose
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = softPink,
                    contentColor = Color.White
                )
            ) {
                Text(if (editandoProdutoId != null) "Atualizar Estoque" else "Salvar Estoque")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Itens no Estoque", style = MaterialTheme.typography.titleLarge, color = darkRose)

            LazyColumn {
                items(estoque) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = softRose
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Produto: ${item.produto.nome}", style = MaterialTheme.typography.titleMedium, color = darkRose)
                            Text("Sabor: ${item.produto.sabor}")
                            Text("Quantidade: ${item.item.quantidade}")

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        selectedProduto = item.produto
                                        quantidade = item.item.quantidade.toString()
                                        editandoProdutoId = item.produto.produtoId
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = softPink,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Editar")
                                }

                                Button(
                                    onClick = { confirmarExclusao(item) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = softRed,
                                        contentColor = Color.White
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
                TextButton(
                    onClick = { excluirEstoque() }
                ) {
                    Text("Confirmar", color = softRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            title = { Text("Confirmar Exclusão", color = darkRose) },
            text = { Text("Tem certeza que deseja excluir este item do estoque?") },
            containerColor = softRose,
            tonalElevation = 8.dp
        )
    }
}
