package com.example.sweetjoygeladinhos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.data.Receita
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun ReceitaScreen(navController: NavController) {
    val receitaDao = remember { SweetJoyApp.database.receitaDao() }
    val receitas by receitaDao.listarTodas().collectAsState(initial = emptyList())

    var nome by remember { mutableStateOf("") }
    var ingredientes by remember { mutableStateOf("") }
    var modoPreparo by remember { mutableStateOf("") }
    var quantidadeText by remember { mutableStateOf("") }

    var receitaEditando by remember { mutableStateOf<Receita?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            if (receitaEditando == null) "Nova Receita" else "Editar Receita",
            style = MaterialTheme.typography.headlineSmall
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
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default.copy(
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
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (receitaEditando == null) "Salvar" else "Atualizar")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(receitas) { receita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(receita.nome, style = MaterialTheme.typography.titleMedium)
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
                                Text("Editar")
                            }
                            TextButton(onClick = {
                                scope.launch { receitaDao.deletar(receita) }
                            }) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }
    }
}
