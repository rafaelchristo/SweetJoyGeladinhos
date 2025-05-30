package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreProdutoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val produtosCollection = db.collection("produtos")

    // Função para buscar todos produtos do Firestore
    suspend fun getProdutos(): List<Produto> {
        val snapshot = produtosCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Produto::class.java)?.copy(produtoId = it.id)
        }
    }

    // Você pode implementar outras funções como add, update, delete conforme precisar
}
