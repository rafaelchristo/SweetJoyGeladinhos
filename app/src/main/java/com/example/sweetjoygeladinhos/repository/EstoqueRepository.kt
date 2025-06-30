package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EstoqueRepository {

    private val db = FirebaseFirestore.getInstance()
    private val estoqueRef = db.collection("estoque")
    private val produtosRef = db.collection("produtos")

    suspend fun adicionarOuAtualizarItem(item: EstoqueItem) {
        estoqueRef.document(item.produtoId).set(item).await()
    }

    suspend fun atualizarQuantidade(produtoId: String, novaQuantidade: Int) {
        estoqueRef.document(produtoId).update("quantidade", novaQuantidade).await()
    }

    suspend fun deletarItem(produtoId: String) {
        estoqueRef.document(produtoId).delete().await()
    }

    suspend fun obterItem(produtoId: String): EstoqueItem? {
        val snapshot = estoqueRef.document(produtoId).get().await()
        return snapshot.toObject(EstoqueItem::class.java)
    }

    suspend fun obterTodos(): List<EstoqueItem> {
        val snapshot = estoqueRef.get().await()
        return snapshot.documents.mapNotNull { it.toObject(EstoqueItem::class.java) }
    }

    suspend fun obterTodosComProduto(): List<EstoqueItemComProduto> {
        val estoqueSnapshot = estoqueRef.get().await()
        val estoqueItems = estoqueSnapshot.toObjects(EstoqueItem::class.java)

        val produtosSnapshot = produtosRef.get().await()
        val produtos = produtosSnapshot.documents.mapNotNull {
            val produto = it.toObject(Produto::class.java)?.copy(id = it.id)
            produto?.let { p -> p.id to p }
        }.toMap()

        return estoqueItems.mapNotNull { item ->
            val produto = produtos[item.produtoId]
            if (produto != null) {
                EstoqueItemComProduto(item, produto)
            } else null
        }
    }

    // ✅ NOVO MÉTODO seguro para subtrair do estoque
    suspend fun subtrairQuantidade(produtoId: String, quantidade: Int) {
        val item = obterItem(produtoId)
        if (item != null && item.quantidade >= quantidade) {
            val novaQuantidade = item.quantidade - quantidade
            atualizarQuantidade(produtoId, novaQuantidade)
        }
    }
}
