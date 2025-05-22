import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.sweetjoygeladinhos.R
import com.example.sweetjoygeladinhos.SweetJoyApp
import com.example.sweetjoygeladinhos.model.Produto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(navController: NavController) {
    val context = LocalContext.current
    val produtoDao = remember { SweetJoyApp.database.produtoDao() }

    var produtos by remember { mutableStateOf(emptyList<Produto>()) }
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
        imagemUri = uri?.toString()
    }

    LaunchedEffect(Unit) {
        produtos = produtoDao.getAll()
    }

    fun carregarProdutos() {
        coroutineScope.launch {
            produtos = produtoDao.getAll()
        }
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
            carregarProdutos()
            showDialog = false
            nome = ""; sabor = ""; preco = ""; imagemUri = null
            produtoEmEdicao = null
        }
    }

    fun deletarProduto() {
        produtoParaExcluir?.let {
            coroutineScope.launch {
                produtoDao.delete(it)
                carregarProdutos()
                showDeleteConfirm = false
                produtoParaExcluir = null
            }
        }
    }

    fun gerarMensagemCatalogo(produtos: List<Produto>): String {
        val sb = StringBuilder()
        sb.append("🍦 *Catálogo SweetJoyGeladinhos* 🍦\n\n")
        produtos.forEachIndexed { index, produto ->
            sb.append("${index + 1}️⃣ ${produto.nome}\n")
            sb.append("Sabor: ${produto.sabor} \n")
            sb.append("Preço: R$ %.2f\n\n".format(produto.preco))
        }
        sb.append("Faça seu pedido agora! 😋")
        return sb.toString()
    }

    fun compartilharCatalogoNoWhatsApp(context: Context, mensagem: String) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Produtos", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    produtoEmEdicao = null
                    nome = ""; sabor = ""; preco = ""; imagemUri = null
                    showDialog = true
                },
                modifier = Modifier.shadow(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Lista de Produtos", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(produtos) { produto ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { imagemEmTelaCheia = produto.imagemUri },
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            produto.imagemUri?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                                Text(produto.sabor, fontSize = 12.sp)
                                Text("R$ %.2f".format(produto.preco), fontSize = 12.sp)
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = {
                                        produtoEmEdicao = produto
                                        nome = produto.nome
                                        sabor = produto.sabor
                                        preco = produto.preco.toString()
                                        imagemUri = produto.imagemUri
                                        showDialog = true
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(
                                    onClick = {
                                        produtoParaExcluir = produto
                                        showDeleteConfirm = true
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val mensagem = gerarMensagemCatalogo(produtos)
                    compartilharCatalogoNoWhatsApp(context, mensagem)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compartilhar Catálogo no WhatsApp")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { salvarProduto() }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text(if (produtoEmEdicao == null) "Novo Produto" else "Editar Produto") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
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
                        label = { Text("Preço") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.shadow(4.dp)
                    ) {
                        Text(if (imagemUri != null) "Imagem selecionada" else "Selecionar Imagem")
                    }
                }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            confirmButton = {
                TextButton(onClick = { deletarProduto() }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar Exclusão") },
            text = {
                Text("Tem certeza que deseja excluir o produto \"${produtoParaExcluir?.nome}\"?")
            }
        )
    }

    imagemEmTelaCheia?.let { uri ->
        Dialog(onDismissRequest = { imagemEmTelaCheia = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { imagemEmTelaCheia = null },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProdutosScreen() {
    ProdutosScreen(navController = rememberNavController())
}
