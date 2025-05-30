package com.example.sweetjoygeladinhos.ui

import androidx.compose.foundation.lazy.items //
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sweetjoygeladinhos.model.Promocao
import com.example.sweetjoygeladinhos.viewmodel.PromocaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromocaoScreen(
    navController: NavHostController,
    viewModel: PromocaoViewModel
) {
    val promocoes by viewModel.promocoes.collectAsState()
    val erro by viewModel.erro.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var promocaoParaExcluir by remember { mutableStateOf<Promocao?>(null) }
    var promocaoEditando by remember { mutableStateOf<Promocao?>(null) }

    // Estados do formulário
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(promocaoEditando) {
        promocaoEditando?.let {
            titulo = it.titulo
            descricao = it.descricao
            validade = it.validade
            imagemUri = it.imagemUri
        } ?: run {
            titulo = ""
            descricao = ""
            validade = ""
            imagemUri = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Promoções", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Formulário
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = validade,
            onValueChange = { validade = it },
            label = { Text("Validade") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = imagemUri ?: "",
            onValueChange = { imagemUri = it },
            label = { Text("Imagem URI") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                if (titulo.isBlank() || descricao.isBlank() || validade.isBlank()) return@Button

                val promocao = promocaoEditando?.copy(
                    titulo = titulo,
                    descricao = descricao,
                    validade = validade,
                    imagemUri = imagemUri
                ) ?: Promocao(
                    titulo = titulo,
                    descricao = descricao,
                    validade = validade,
                    imagemUri = imagemUri
                )

                if (promocaoEditando == null) {
                    viewModel.adicionarPromocao(promocao)
                } else {
                    viewModel.atualizarPromocao(promocao)
                    promocaoEditando = null
                }

                // Limpa formulário
                titulo = ""
                descricao = ""
                validade = ""
                imagemUri = null
            }) {
                Text(if (promocaoEditando == null) "Adicionar" else "Salvar")
            }

            if (promocaoEditando != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    promocaoEditando = null
                    titulo = ""
                    descricao = ""
                    validade = ""
                    imagemUri = null
                }) {
                    Text("Cancelar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (erro != null) {
            Text(text = "Erro: $erro", color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(promocoes) { promocao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(promocao.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(promocao.descricao)
                            Text("Validade: ${promocao.validade}", style = MaterialTheme.typography.bodySmall)
                        }
                        Row {
                            TextButton(onClick = { promocaoEditando = promocao }) {
                                Text("Editar")
                            }
                            TextButton(onClick = {
                                promocaoParaExcluir = promocao
                                showDialog = true
                            }) {
                                Text("Excluir", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog && promocaoParaExcluir != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar exclusão") },
            text = { Text("Tem certeza que deseja excluir a promoção \"${promocaoParaExcluir?.titulo}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    promocaoParaExcluir?.let {
                        viewModel.deletarPromocao(it.id)
                    }
                    showDialog = false
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Não")
                }
            }
        )
    }
}
