package com.example.sweetjoygeladinhos.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweetjoygeladinhos.model.Evento
import com.example.sweetjoygeladinhos.viewmodel.EventoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeiraEventosScreen(
    navController: NavController,
    viewModel: EventoViewModel = viewModel()
) {
    val eventos by viewModel.eventos.collectAsState()
    val carregando by viewModel.carregando.collectAsState()

    var eventoParaDeletar by remember { mutableStateOf<Evento?>(null) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feira e Eventos") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("criar_evento") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Criar novo evento")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Eventos Criados",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (carregando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (eventos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum evento criado ainda.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(eventos) { evento ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    navController.navigate("detalhes_evento/${evento.id}")
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(evento.nome, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "Data: ${dateFormat.format(Date(evento.data))}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Itens: ${evento.itens.size}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Row {
                                    IconButton(onClick = {
                                        navController.navigate("criar_evento?eventoId=${evento.id}")
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = {
                                        eventoParaDeletar = evento
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Deletar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (eventoParaDeletar != null) {
            AlertDialog(
                onDismissRequest = { eventoParaDeletar = null },
                title = { Text("Excluir Evento") },
                text = { Text("Deseja realmente excluir o evento \"${eventoParaDeletar?.nome}\"?") },
                confirmButton = {
                    TextButton(onClick = {
                        eventoParaDeletar?.id?.let { viewModel.deletarEvento(it) }
                        eventoParaDeletar = null
                    }) {
                        Text("Excluir", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { eventoParaDeletar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // Recarregar ao voltar para a tela
    LaunchedEffect(Unit) {
        viewModel.carregarEventos()
    }
}
