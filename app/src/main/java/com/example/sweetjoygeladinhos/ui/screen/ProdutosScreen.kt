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
import coil.request.ImageRequest
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.utils.saveImageToInternalStorage
import com.example.sweetjoygeladinhos.viewmodel.ProdutoViewModel
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.foundation.background

@Composable
fun ProdutosScreen(
    navController: NavController,
    viewModel: ProdutoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    SweetJoyGeladinhosTheme {
        ProdutosScreenContent(navController, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreenContent(
    navController: NavController,
    viewModel: ProdutoViewModel
) {
    val produtos by viewModel.produtos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState(initial = false) // <- loading vindo do ViewModel
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var sabor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }
    var produtoEmEdicao by remember { mutableStateOf<Produto?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }
    var imagemEmTelaCheia by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedUri = saveImageToInternalStorage(context, it)
            imagemUri = savedUri
        }
    }

    fun limparCampos() {
        nome = ""
        sabor = ""
        preco = ""
        imagemUri = null
        produtoEmEdicao = null
    }

    fun salvarProduto() {
        val precoDouble = preco.toDoubleOrNull()
        if (precoDouble == null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Preço inválido")
            }
            return
        }

        val produto = Produto(
            id = produtoEmEdicao?.id ?: "", // Ajuste para Firestore ID
            nome = nome,
            sabor = sabor,
            preco = precoDouble,
            imagemUri = imagemUri
        )

        coroutineScope.launch {
            val sucesso = if (produtoEmEdicao == null) {
                viewModel.adicionarProduto(produto)
            } else {
                viewModel.atualizarProduto(produto)
            }

            if (sucesso) {
                showDialog = false
                limparCampos()
            } else {
                snackbarHostState.showSnackbar("Erro ao salvar produto")
            }
        }
    }

    fun deletarProduto() {
        produtoParaExcluir?.let { produto ->
            coroutineScope.launch {
                val sucesso = viewModel.deletarProduto(produto.id)
                if (sucesso) {
                    showDeleteConfirm = false
                    produtoParaExcluir = null
                } else {
                    snackbarHostState.showSnackbar("Erro ao deletar produto")
                }
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    limparCampos()
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(produtos) { produto ->
                    ProdutoCard(
                        produto = produto,
                        onEdit = {
                            produtoEmEdicao = produto
                            nome = produto.nome
                            sabor = produto.sabor
                            preco = produto.preco.toString()
                            imagemUri = produto.imagemUri
                            showDialog = true
                        },
                        onDelete = {
                            produtoParaExcluir = produto
                            showDeleteConfirm = true
                        },
                        onImageClick = { imagemEmTelaCheia = produto.imagemUri }
                    )
                }
            }

            // Indicador de loading sobreposto na tela enquanto carrega
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    // Diálogos e outros componentes abaixo (sem alteração)...

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (produtoEmEdicao == null) "Adicionar Produto" else "Editar Produto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
                    OutlinedTextField(value = sabor, onValueChange = { sabor = it }, label = { Text("Sabor") })
                    OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                            Text("Selecionar Imagem")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        imagemUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(File(uri)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = { salvarProduto() }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Confirmar exclusão") },
            text = { Text("Tem certeza que deseja excluir este produto?") },
            confirmButton = {
                FilledTonalButton(onClick = { deletarProduto() }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    imagemEmTelaCheia?.let { uri ->
        AlertDialog(
            onDismissRequest = { imagemEmTelaCheia = null },
            confirmButton = {
                TextButton(onClick = { imagemEmTelaCheia = null }) {
                    Text("Fechar")
                }
            },
            text = {
                Image(
                    painter = rememberAsyncImagePainter(File(uri)),
                    contentDescription = "Imagem em Tela Cheia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun ProdutoCard(
    produto: Produto,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onImageClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable { onImageClick() },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            produto.imagemUri?.let { uriString ->
                val file = File(uriString)
                Image(
                    painter = rememberAsyncImagePainter(file),
                    contentDescription = produto.nome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = produto.nome, style = MaterialTheme.typography.titleMedium)
            Text(text = "Sabor: ${produto.sabor}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "R$ %.2f".format(produto.preco), style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}
