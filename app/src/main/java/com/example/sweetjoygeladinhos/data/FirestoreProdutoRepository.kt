package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreProdutoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val produtosCollection = db.collection("produtos")

    suspend fun getProdutos(): List<Produto> {
        val snapshot = produtosCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Produto::class.java)?.copy(produtoId = it.id)
        }
    }

    suspend fun addProduto(produto: Produto) {
        produtosCollection.add(produto).await()
    }

    suspend fun updateProduto(produto: Produto) {
        if (produto.produtoId.isNotBlank()) {
            produtosCollection.document(produto.produtoId).set(produto).await()
        } else {
            throw IllegalArgumentException("Produto ID inválido para atualização.")
        }
    }

    suspend fun deleteProduto(produtoId: String) {
        if (produtoId.isNotBlank()) {
            produtosCollection.document(produtoId).delete().await()
        } else {
            throw IllegalArgumentException("Produto ID inválido para exclusão.")
        }
    }
}
