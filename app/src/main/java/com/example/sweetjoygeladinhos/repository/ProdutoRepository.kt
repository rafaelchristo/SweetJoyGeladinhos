package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProdutoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val produtosRef = db.collection("produtos")

    suspend fun adicionarProduto(produto: Produto) {
        val doc = produtosRef.document()
        val produtoComId = produto.copy(id = doc.id)
        doc.set(produtoComId).await()
    }

    suspend fun obterProdutos(): List<Produto> {
        val snapshot = produtosRef.get().await()
        return snapshot.toObjects(Produto::class.java)
    }

    suspend fun atualizarProduto(produto: Produto) {
        produtosRef.document(produto.id).set(produto).await()
    }

    suspend fun deletarProduto(id: String) {
        produtosRef.document(id).delete().await()
    }
}
