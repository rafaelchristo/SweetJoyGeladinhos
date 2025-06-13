package com.example.sweetjoygeladinhos.ui.screen

import SweetJoyGeladinhosTheme
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.sweetjoygeladinhos.viewmodel.PedidosViewModel

@Composable
fun PedidosScreen(viewModel: PedidosViewModel) {
    SweetJoyGeladinhosTheme {
        PedidosScreenContent(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosScreenContent(viewModel: PedidosViewModel) {
    val context = LocalContext.current

    // Corrigido: coletar o StateFlow com collectAsState() e dar um valor inicial
    val produtos by viewModel.produtos.collectAsState(initial = emptyList())
    val produtosSelecionados by viewModel.produtosSelecionados.collectAsState(initial = emptyMap())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fazer Pedido") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(produtos) { produto ->
                    val quantidade = produtosSelecionados[produto] ?: 0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            produto.imagemUri?.let { uriString ->
                                val imagePainter = rememberAsyncImagePainter(model = Uri.parse(uriString))
                                Image(
                                    painter = imagePainter,
                                    contentDescription = "Imagem do Produto",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Text(
                                text = produto.nome,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "R$ %.2f".format(produto.preco),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                IconButton(onClick = {
                                    if (quantidade > 0) {
                                        viewModel.removerProduto(produto)
                                    }
                                }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Diminuir", tint = MaterialTheme.colorScheme.primary)
                                }

                                Text(
                                    text = "$quantidade",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                IconButton(onClick = {
                                    viewModel.adicionarProduto(produto)
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Aumentar", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.limparPedido() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Limpar")
                }

                Button(
                    onClick = {
                        if (produtosSelecionados.isEmpty()) {
                            Toast.makeText(context, "Selecione ao menos um produto.", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.salvarPedido()
                            Toast.makeText(context, "Pedido registrado com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Fazer Pedido")
                }
            }
        }
    }
}
