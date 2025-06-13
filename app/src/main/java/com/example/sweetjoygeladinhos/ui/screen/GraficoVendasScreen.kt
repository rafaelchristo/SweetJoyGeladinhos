package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficoVendasScreen(navController: NavController) {

    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    // Triple: dataFormatada, produtoNome, quantidade
    var vendasRegistradas by remember {
        mutableStateOf<List<Triple<String, String, Int>>>(emptyList())
    }

    val cores = listOf(
        Color(0xFFFFC1CC), Color(0xFF8B1E3F), Color(0xFF7FC8A9),
        Color(0xFF6C5B7B), Color(0xFFF67280), Color(0xFF355C7D), Color(0xFFFFB347)
    )

    fun carregarVendas() {
        coroutineScope.launch {
            try {
                val vendasSnapshot = firestore.collection("vendas").get().await()
                val produtosSnapshot = firestore.collection("produtos").get().await()

                val mapaIdParaNome = produtosSnapshot.documents.associate {
                    it.id to (it.getString("nome") ?: "Desconhecido")
                }

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val lista = mutableListOf<Triple<String, String, Int>>()

                for (doc in vendasSnapshot.documents) {
                    val venda = doc.toObject(Venda::class.java)
                    if (venda != null) {
                        val dataFormatada = dateFormat.format(Date(venda.dataVenda))
                        venda.produtos.forEach { (produtoId, qtd) ->
                            val nome = mapaIdParaNome[produtoId] ?: "Desconhecido"
                            lista.add(Triple(dataFormatada, nome, qtd))
                        }
                    }
                }

                vendasRegistradas = lista

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) { carregarVendas() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráfico de Vendas", fontSize = 22.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B1E3F))
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

                val vendasAgrupadas = vendasRegistradas
                    .groupBy { it.first to it.second } // (data, produto)
                    .map { (key, grupo) ->
                        Triple(key.first, key.second, grupo.sumOf { it.third }) // data, produto, total
                    }

                val totalVendido = vendasRegistradas.sumOf { it.third }

                Text(
                    "Total de produtos vendidos: $totalVendido",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF8B1E3F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val produtosUnicos = vendasAgrupadas.map { it.second }.distinct()
                val mapaCores = produtosUnicos.mapIndexed { index, produto ->
                    produto to cores[index % cores.size]
                }.toMap()

                val barWidth = with(LocalDensity.current) { 20.dp.toPx() }
                val maxQuantidade = vendasAgrupadas.maxOf { it.third }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val canvasHeight = size.height
                    val spacing = 30f

                    vendasAgrupadas.forEachIndexed { index, triple ->
                        val barHeight = (triple.third / maxQuantidade.toFloat()) * canvasHeight
                        val left = spacing + index * (barWidth + spacing)
                        val top = canvasHeight - barHeight
                        val cor = mapaCores[triple.second] ?: Color.Gray

                        drawRect(
                            color = cor,
                            topLeft = Offset(left, top),
                            size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Quantidade por Produto e Data:",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF8B1E3F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                vendasAgrupadas.forEach { (data, produto, qtd) ->
                    val cor = mapaCores[produto] ?: Color.Gray
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(cor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "$data - $produto: $qtd unidades",
                            fontSize = 14.sp,
                            color = Color.DarkGray
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
