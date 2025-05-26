import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import com.example.sweetjoygeladinhos.utils.saveImageToInternalStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(navController: NavController) {
    val context = LocalContext.current
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }
    val produtos by produtoDao.getAll().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }
    var produtoEmEdicao by remember { mutableStateOf<Produto?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }
    var imagemEmTelaCheia by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedUri = saveImageToInternalStorage(context, it)
            imagemUri = savedUri
        }
    }

    fun saveImageToInternalStorage(context: android.content.Context, uri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "img_${System.currentTimeMillis()}.jpg")
        val outputStream: OutputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    fun salvarProduto() {
        val precoDouble = preco.toDoubleOrNull() ?: return
        val produto = Produto(
            produtoId = produtoEmEdicao?.produtoId ?: 0L,
            nome = nome,
            sabor = sabor,
            preco = precoDouble,
            imagemUri = imagemUri
        )
        coroutineScope.launch {
            if (produtoEmEdicao == null) {
                produtoDao.insert(produto)
            } else {
                produtoDao.update(produto)
            }
            showDialog = false
            nome = ""; sabor = ""; preco = ""; imagemUri = null
            produtoEmEdicao = null
        }
    }

    fun deletarProduto() {
        produtoParaExcluir?.let {
            coroutineScope.launch {
                produtoDao.delete(it)
                showDeleteConfirm = false
                produtoParaExcluir = null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produtos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                produtoEmEdicao = null
                nome = ""; sabor = ""; preco = ""; imagemUri = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(produtos) { produto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { imagemEmTelaCheia = produto.imagemUri },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        produto.imagemUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(File(uri)),
                                contentDescription = "Imagem do Produto",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickable { imagemEmTelaCheia = uri },
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = produto.nome, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = produto.sabor, fontSize = 14.sp)
                        Text(text = "R$ %.2f".format(produto.preco), fontSize = 14.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = {
                                produtoEmEdicao = produto
                                nome = produto.nome
                                sabor = produto.sabor
                                preco = produto.preco.toString()
                                imagemUri = produto.imagemUri
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                produtoParaExcluir = produto
                                showDeleteConfirm = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
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
            title = { Text(if (produtoEmEdicao == null) "Adicionar Produto" else "Editar Produto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") }
                    )
                    OutlinedTextField(
                        value = sabor,
                        onValueChange = { sabor = it },
                        label = { Text("Sabor") }
                    )
                    OutlinedTextField(
                        value = preco,
                        onValueChange = { preco = it },
                        label = { Text("Preço") }
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                            Text("Selecionar Imagem")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        imagemUri?.let {
                            Text(text = "Imagem selecionada", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { salvarProduto() }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Confirmar exclusão") },
            text = { Text("Tem certeza que deseja excluir este produto?") },
            confirmButton = {
                Button(onClick = { deletarProduto() }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (imagemEmTelaCheia != null) {
        AlertDialog(
            onDismissRequest = { imagemEmTelaCheia = null },
            confirmButton = {
                TextButton(onClick = { imagemEmTelaCheia = null }) {
                    Text("Fechar")
                }
            },
            text = {
                imagemEmTelaCheia?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(File(uri)),
                        contentDescription = "Imagem em Tela Cheia",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        )
    }
}
