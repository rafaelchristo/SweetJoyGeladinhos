package com.example.sweetjoygeladinhos.utils

import android.content.Context
import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun preencherComProdutosDeTeste(context: Context) {
    val db = FirebaseFirestore.getInstance()
    val produtosRef = db.collection("produtos")

    val produtosTeste = listOf(
        Produto(nome = "Geladinho de Morango", sabor = "Morango", preco = 2.50, imagemUri = null),
        Produto(nome = "Geladinho de Chocolate", sabor = "Chocolate", preco = 3.00, imagemUri = null),
        Produto(nome = "Geladinho de Coco", sabor = "Coco", preco = 2.75, imagemUri = null),
        Produto(nome = "Geladinho de Maracujá", sabor = "Maracujá", preco = 2.50, imagemUri = null),
    )

    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Evita duplicar dados se já houver produtos
            val existentes = produtosRef.get().await()
            if (existentes.isEmpty) {
                for (produto in produtosTeste) {
                    val doc = produtosRef.document()
                    val produtoComId = produto.copy(id = doc.id)
                    doc.set(produtoComId).await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
