package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreEstoqueRepository {

    private val db = FirebaseFirestore.getInstance()
    private val estoqueCollection = db.collection("estoque")

    // Buscar todos os itens do estoque
    suspend fun getEstoque(): List<EstoqueItem> {
        val snapshot = estoqueCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(EstoqueItem::class.java)?.copy(id = it.id)
        }
    }

    // Adicionar item no estoque
    suspend fun addEstoqueItem(item: EstoqueItem) {
        estoqueCollection.add(item).await()
    }

    // Atualizar item do estoque
    suspend fun updateEstoqueItem(item: EstoqueItem) {
        if (item.id.isNotBlank()) {
            estoqueCollection.document(item.id).set(item).await()
        }
    }

    // Deletar item do estoque
    suspend fun deleteEstoqueItem(id: String) {
        estoqueCollection.document(id).delete().await()
    }
}
