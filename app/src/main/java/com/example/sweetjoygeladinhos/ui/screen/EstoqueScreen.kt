package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.model.EstoqueItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstoqueScreen() {
    var estoqueList by remember {
        mutableStateOf(
            listOf(
                EstoqueItem(1, Produto(1, "Geladinho", "Morango com Chocolate Branco", 2.5), 30),
                EstoqueItem(2, Produto(2, "Geladinho", "Chocolate com Doce de Leite", 3.0), 20),
                EstoqueItem(3, Produto(3, "Geladinho", "Ninho com Nutella", 2.8), 25),
                EstoqueItem(4, Produto(4, "Geladinho", "Oreo", 2.8), 25)
            )
        )
    }

    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    fun adicionarProduto() {
        val precoDouble = preco.toDoubleOrNull() ?: return
        val quantidadeInt = quantidade.toIntOrNull() ?: return

        val novoProduto = Produto(
            produtoId = estoqueList.size + 1,
            nome = nome,
            sabor = sabor,
            preco = precoDouble
        )
        val novoEstoqueItem = EstoqueItem(
            estoqueId = (estoqueList.size + 1).toLong(),
            produto = novoProduto,
            quantidade = quantidadeInt
        )
        estoqueList = estoqueList + novoEstoqueItem

        nome = ""
        sabor = ""
        preco = ""
        quantidade = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estoque", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Formulário
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Produto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = sabor,
                onValueChange = { sabor = it },
                label = { Text("Sabor") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = preco,
                onValueChange = { preco = it },
                label = { Text("Preço (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = quantidade,
                onValueChange = { quantidade = it },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { adicionarProduto() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Produto")
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                "Produtos no Estoque",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(estoqueList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(item.produto.nome, fontWeight = FontWeight.Bold)
                            Text("Sabor: ${item.produto.sabor}")
                            Text("Preço: R$ %.2f".format(item.produto.preco))
                            Text("Quantidade: ${item.quantidade}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewEstoqueScreen() {
    EstoqueScreen()
}