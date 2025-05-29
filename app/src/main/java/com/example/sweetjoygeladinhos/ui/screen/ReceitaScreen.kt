package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.data.Receita
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitaScreen(navController: NavController) {
    // Esquema de cores
    val softPink = Color(0xFFFFC1CC)
    val softRose = Color(0xFFFFD6E0)
    val darkRose = Color(0xFF8B1E3F)

    val receitaDao = remember { SweetJoyApp.database.receitaDao() }
    val receitas by receitaDao.listarTodas().collectAsState(initial = emptyList())

    var nome by remember { mutableStateOf("") }
    var ingredientes by remember { mutableStateOf("") }
    var modoPreparo by remember { mutableStateOf("") }
    var quantidadeText by remember { mutableStateOf("") }

    var receitaEditando by remember { mutableStateOf<Receita?>(null) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receitas", fontSize = MaterialTheme.typography.titleLarge.fontSize) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = softPink,
                    titleContentColor = Color.White
                )
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
            Text(
                if (receitaEditando == null) "Nova Receita" else "Editar Receita",
                style = MaterialTheme.typography.headlineSmall,
                color = darkRose
            )

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Receita") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ingredientes,
                onValueChange = { ingredientes = it },
                label = { Text("Ingredientes") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = modoPreparo,
                onValueChange = { modoPreparo = it },
                label = { Text("Modo de Preparo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = quantidadeText,
                onValueChange = { quantidadeText = it.filter { char -> char.isDigit() } },
                label = { Text("Quantidade de unidades") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val quantidade = quantidadeText.toIntOrNull() ?: 0
                    scope.launch {
                        if (receitaEditando == null) {
                            receitaDao.inserir(
                                Receita(
                                    nome = nome,
                                    ingredientes = ingredientes,
                                    modoPreparo = modoPreparo,
                                    quantidade = quantidade
                                )
                            )
                        } else {
                            receitaDao.atualizar(
                                receitaEditando!!.copy(
                                    nome = nome,
                                    ingredientes = ingredientes,
                                    modoPreparo = modoPreparo,
                                    quantidade = quantidade
                                )
                            )
                            receitaEditando = null
                        }
                        nome = ""
                        ingredientes = ""
                        modoPreparo = ""
                        quantidadeText = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = softPink,
                    contentColor = Color.White
                )
            ) {
                Text(if (receitaEditando == null) "Salvar" else "Atualizar")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = softPink)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(receitas) { receita ->
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
                            Text(receita.nome, style = MaterialTheme.typography.titleMedium, color = darkRose)
                            Text("Ingredientes: ${receita.ingredientes}")
                            Text("Modo de Preparo: ${receita.modoPreparo}")
                            Text("Quantidade: ${receita.quantidade} unidade(s)")

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(onClick = {
                                    receitaEditando = receita
                                    nome = receita.nome
                                    ingredientes = receita.ingredientes
                                    modoPreparo = receita.modoPreparo
                                    quantidadeText = receita.quantidade.toString()
                                }) {
                                    Text("Editar", color = darkRose)
                                }
                                TextButton(onClick = {
                                    scope.launch { receitaDao.deletar(receita) }
                                }) {
                                    Text("Excluir", color = darkRose)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
