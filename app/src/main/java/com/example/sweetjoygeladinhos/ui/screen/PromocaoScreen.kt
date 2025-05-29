package com.example.sweetjoygeladinhos.ui.screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.Promocao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromocaoScreen(navController: NavController) {
    // Esquema de cores
    val softPink = Color(0xFFFFC1CC)
    val softRose = Color(0xFFFFD6E0)
    val darkRose = Color(0xFF8B1E3F)

    val context = LocalContext.current
    val promocaoDao = remember { SweetJoyApp.database.promocaoDao() }
    val coroutineScope = rememberCoroutineScope()

    var promocoes by remember { mutableStateOf(emptyList<Promocao>()) }

    var showDialog by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }
    var promocaoEmEdicao by remember { mutableStateOf<Promocao?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imagemUri = uri?.toString() }

    fun carregarPromocoes() {
        coroutineScope.launch {
            promocoes = promocaoDao.getAll()
        }
    }

    fun salvarPromocao() {
        val promocao = Promocao(
            id = promocaoEmEdicao?.id ?: 0,
            titulo = titulo,
            descricao = descricao,
            validade = validade,
            imagemUri = imagemUri
        )
        coroutineScope.launch {
            if (promocaoEmEdicao == null) {
                promocaoDao.insert(promocao)
            } else {
                promocaoDao.update(promocao)
            }
            carregarPromocoes()
            showDialog = false
            titulo = ""; descricao = ""; validade = ""; imagemUri = null
            promocaoEmEdicao = null
        }
    }

    fun gerarMensagem(promocao: Promocao): String {
        return """
            🎉 *${promocao.titulo}* 🎉
            
            ${promocao.descricao}
            
            ✅ Válido até: ${promocao.validade}
            
            Aproveite e faça seu pedido! 😋
        """.trimIndent()
    }

    fun enviarWhatsApp(mensagem: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mensagem)
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "WhatsApp não está instalado.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) { carregarPromocoes() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Promoções", fontSize = 22.sp, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = softPink
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    promocaoEmEdicao = null
                    titulo = ""; descricao = ""; validade = ""; imagemUri = null
                    showDialog = true
                },
                containerColor = softPink,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova Promoção")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(promocoes) { promocao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = softRose
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            promocao.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            color = darkRose
                        )
                        Text(promocao.descricao, color = Color.Black)
                        Text("Validade: ${promocao.validade}", color = Color.Black)

                        promocao.imagemUri?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .padding(top = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                promocaoEmEdicao = promocao
                                titulo = promocao.titulo
                                descricao = promocao.descricao
                                validade = promocao.validade
                                imagemUri = promocao.imagemUri
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = darkRose)
                            }
                            IconButton(onClick = {
                                enviarWhatsApp(gerarMensagem(promocao))
                            }) {
                                Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = darkRose)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { salvarPromocao() }) {
                    Text("Salvar", color = darkRose)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            title = {
                Text(
                    if (promocaoEmEdicao == null) "Nova Promoção" else "Editar Promoção",
                    color = darkRose
                )
            },
            text = {
                Column {
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
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = darkRose
                        )
                    ) {
                        Text(if (imagemUri != null) "Imagem Selecionada" else "Selecionar Imagem")
                    }
                }
            }
        )
    }
}
