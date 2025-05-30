package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.model.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficoVendasScreen(navController: NavController) {

    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    var vendasRegistradas by remember {
        mutableStateOf<List<Triple<Venda, String, String>>>(emptyList())
    }

    // Lista de cores para diferenciar os produtos
    val cores = listOf(
        Color(0xFFFFC1CC), // Rosa claro
        Color(0xFF8B1E3F), // Vinho
        Color(0xFF7FC8A9), // Verde
        Color(0xFF6C5B7B), // Roxo
        Color(0xFFF67280), // Coral
        Color(0xFF355C7D), // Azul escuro
        Color(0xFFFFB347)  // Laranja
    )

    fun carregarVendas() {
        coroutineScope.launch {
            try {
                val vendasSnapshot = firestore.collection("vendas").get().await()
                val vendas = vendasSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Venda::class.java)
                }

                val produtosSnapshot = firestore.collection("produtos").get().await()
                val produtosMap = produtosSnapshot.documents.associateBy(
                    { it.id },
                    { it.getString("nome") ?: "Desconhecido" }
                )

                val vendasComNomes = vendas.mapNotNull { venda ->
                    val nomeProduto = produtosMap[venda.produtoId]
                    nomeProduto?.let {
                        Triple(venda, it, venda.produtoId)
                    }
                }

                vendasRegistradas = vendasComNomes
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarVendas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráfico de Vendas", fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFC1CC),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (vendasRegistradas.isNotEmpty()) {

                val vendasPorDataProduto = vendasRegistradas.groupBy {
                    it.first.dataVenda to it.second
                }.map { (key, vendas) ->
                    Triple(key.first, key.second, vendas.sumOf { it.first.quantidade })
                }

                val totalVendido = vendasRegistradas.sumOf { it.first.quantidade }

                Text(
                    "Total de produtos vendidos: $totalVendido",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF8B1E3F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mapeia cores únicas para cada produto
                val produtosUnicos = vendasPorDataProduto.map { it.second }.distinct()
                val mapaCores = produtosUnicos.mapIndexed { index, produto ->
                    produto to cores[index % cores.size]
                }.toMap()

                val barWidth = with(LocalDensity.current) { 20.dp.toPx() }
                val maxQuantidade = vendasPorDataProduto.maxOf { it.third }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val canvasHeight = size.height
                    val spacing = 30f

                    vendasPorDataProduto.forEachIndexed { index, triple ->
                        val barHeight = (triple.third / maxQuantidade.toFloat()) * canvasHeight
                        val left = spacing + index * (barWidth + spacing)
                        val top = canvasHeight - barHeight
                        val cor = mapaCores[triple.second] ?: Color.Gray

                        drawRect(
                            color = cor,
                            topLeft = Offset(left, top),
                            size = androidx.compose.ui.geometry.Size(
                                width = barWidth,
                                height = barHeight
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Quantidade por Produto e Data:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF8B1E3F)
                )

                vendasPorDataProduto.forEach { (data, produto, qtd) ->
                    val cor = mapaCores[produto] ?: Color.Gray
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .padding(end = 4.dp)
                                .background(cor)
                        )
                        Text(
                            "$data - $produto: $qtd unidades",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Text(
                    "Nenhuma venda registrada para exibir no gráfico.",
                    color = Color.Gray
                )
            }
        }
    }
}
