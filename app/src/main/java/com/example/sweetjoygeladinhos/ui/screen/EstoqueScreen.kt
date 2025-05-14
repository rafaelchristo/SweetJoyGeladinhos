package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.SweetJoyApp
import kotlinx.coroutines.launch
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen() {
    val estoqueDao = remember { SweetJoyApp.database.estoqueDao() }

    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var estoqueList by remember { mutableStateOf(emptyList<EstoqueItemComProduto>()) }

    val scope = rememberCoroutineScope()

    // Carrega o estoque ao iniciar
    LaunchedEffect(true) {
        estoqueList = estoqueDao.getAll()
    }

    fun adicionarProduto() {
        val precoDouble = preco.toDoubleOrNull() ?: return
        val quantidadeInt = quantidade.toIntOrNull() ?: return

        val produto = Produto(nome = nome, sabor = sabor, preco = precoDouble, categoria = "Geladinho")
        scope.launch {
            estoqueDao.insert(produto, quantidadeInt)
            estoqueList = estoqueDao.getAll()
            nome = ""
            sabor = ""
            preco = ""
            quantidade = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Estoque") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = sabor,
                onValueChange = { sabor = it },
                label = { Text("Sabor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = preco,
                onValueChange = { preco = it },
                label = { Text("Preço") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { adicionarProduto() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Produto")
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(estoqueList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.produto.nome, style = MaterialTheme.typography.titleMedium)
                            Text("Sabor: ${item.produto.sabor}")
                            Text("Preço: R$ %.2f".format(item.produto.preco))
                            Text("Quantidade: ${item.estoqueItem.quantidade}")
                        }
                    }
                }
            }
        }
    }
}