package com.example.sweetjoygeladinhos.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.Produto
import com.example.sweetjoygeladinhos.network.CloudinaryService
import com.example.sweetjoygeladinhos.repository.ProdutoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.UUID

class ProdutoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProdutoRepository()

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            carregarProdutos()
        }
    }

    suspend fun carregarProdutos() {
        _isLoading.value = true
        try {
            _produtos.value = repository.obterProdutos()
        } catch (e: Exception) {
            Log.e("ProdutoViewModel", "Erro ao carregar produtos", e)
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun uploadImageToCloudinary(uri: Uri): String {
        val context = getApplication<Application>().applicationContext

        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IOException("Não foi possível abrir o InputStream da imagem.")

        val tempFile = File.createTempFile("cloudinary_upload", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output -> inputStream.copyTo(output) }

        val requestFile = okhttp3.RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            tempFile
        )
        val multipartBody = okhttp3.MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

        val uploadPreset = okhttp3.RequestBody.create(okhttp3.MultipartBody.FORM, "produtos_unsigned")

        val service = CloudinaryService.create()
        val response = service.uploadImage(multipartBody, uploadPreset)

        if (response.isSuccessful) {
            val body = response.body()
            return body?.secure_url ?: throw IOException("Resposta sem URL da imagem.")
        } else {
            throw IOException("Erro ao fazer upload para Cloudinary: ${response.errorBody()?.string()}")
        }
    }

    fun salvarProdutoComImagem(
        produto: Produto,
        imagemUriLocal: Uri?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val produtoComImagem = if (imagemUriLocal != null) {
                    Log.d("ProdutoViewModel", "Upload imagem requisitado")
                    val url = uploadImageToCloudinary(imagemUriLocal)
                    produto.copy(imagemUri = url)
                } else produto

                if (produtoComImagem.id.isEmpty()) {
                    Log.d("ProdutoViewModel", "Adicionando novo produto: ${produtoComImagem.nome}")
                    repository.adicionarProduto(produtoComImagem)
                } else {
                    Log.d("ProdutoViewModel", "Atualizando produto: ${produtoComImagem.id}")
                    repository.atualizarProduto(produtoComImagem)
                }

                carregarProdutos()
                onResult(true)
            } catch (e: Exception) {
                Log.e("ProdutoViewModel", "Erro ao salvar produto", e)
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletarProduto(id: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deletarProduto(id)
                carregarProdutos()
                onResult(true)
            } catch (e: Exception) {
                Log.e("ProdutoViewModel", "Erro ao deletar produto", e)
                onResult(false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
