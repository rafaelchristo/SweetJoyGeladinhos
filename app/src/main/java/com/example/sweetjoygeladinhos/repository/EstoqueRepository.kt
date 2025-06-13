package com.example.sweetjoygeladinhos.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.sweetjoygeladinhos.model.EstoqueItem

class EstoqueRepository {

    private val db = FirebaseFirestore.getInstance()
    private val estoqueRef = db.collection("estoque")

    suspend fun adicionarOuAtualizarItem(item: EstoqueItem) {
        estoqueRef.document(item.produtoId).set(item).await()
    }

    suspend fun atualizarQuantidade(produtoId: String, novaQuantidade: Int) {
        estoqueRef.document(produtoId).update("quantidade", novaQuantidade).await()
    }

    suspend fun deletarItem(produtoId: String) {
        estoqueRef.document(produtoId).delete().await()
    }

    fun obterTodos(): Flow<List<EstoqueItem>> = flow {
        val snapshot = estoqueRef.get().await()
        val lista = snapshot.documents.mapNotNull { it.toObject(EstoqueItem::class.java) }
        emit(lista)
    }

    suspend fun obterItem(produtoId: String): EstoqueItem? {
        val snapshot = estoqueRef.document(produtoId).get().await()
        return snapshot.toObject(EstoqueItem::class.java)
    }
}
