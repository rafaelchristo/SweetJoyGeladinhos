package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val produtosCollection = db.collection("produtos")

    suspend fun getProdutos(): Result<List<Produto>> {
        return try {
            val snapshot = produtosCollection.get().await()
            val produtos = snapshot.documents.mapNotNull {
                it.toObject(Produto::class.java)?.copy(produtoId = it.id)
            }
            Result.success(produtos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addProduto(produto: Produto): Result<Unit> {
        return try {
            produtosCollection.add(produto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduto(produto: Produto): Result<Unit> {
        return try {
            val docId = produto.produtoId
            produtosCollection.document(docId).set(produto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduto(id: String): Result<Unit> {
        return try {
            produtosCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
