package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.EstoqueItem
import com.example.sweetjoygeladinhos.model.EstoqueItemComProduto
import com.example.sweetjoygeladinhos.model.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EstoqueViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _estoque = MutableStateFlow<List<EstoqueItemComProduto>>(emptyList())
    val estoque: StateFlow<List<EstoqueItemComProduto>> = _estoque

    init {
        carregarProdutos()
        carregarEstoque()
    }

    private fun carregarProdutos() = viewModelScope.launch {
        try {
            val snapshot = db.collection("produtos").get().await()
            _produtos.value = snapshot.toObjects(Produto::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            // Aqui você pode adicionar algum tratamento de erro visível para o usuário, se quiser
        }
    }

    private fun carregarEstoque() = viewModelScope.launch {
        try {
            val estoqueSnapshot = db.collection("estoque").get().await()
            val listaEstoque = estoqueSnapshot.documents.mapNotNull { doc ->
                val item = doc.toObject(EstoqueItem::class.java)
                if (item != null) {
                    val produtoDoc = db.collection("produtos").document(item.produtoId).get().await()
                    val produto = produtoDoc.toObject(Produto::class.java)
                    if (produto != null) {
                        EstoqueItemComProduto(item, produto)
                    } else null
                } else null
            }
            _estoque.value = listaEstoque
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun salvarEstoque(item: EstoqueItem) = viewModelScope.launch {
        try {
            val estoqueRef = db.collection("estoque")
            val query = estoqueRef.whereEqualTo("produtoId", item.produtoId).get().await()
            if (query.isEmpty) {
                estoqueRef.add(item).await()
            } else {
                val docId = query.documents.first().id
                estoqueRef.document(docId).set(item).await()
            }
            carregarEstoque()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun excluirEstoque(item: EstoqueItem) = viewModelScope.launch {
        try {
            val estoqueRef = db.collection("estoque")
            val query = estoqueRef.whereEqualTo("produtoId", item.produtoId).get().await()
            if (!query.isEmpty) {
                estoqueRef.document(query.documents.first().id).delete().await()
                carregarEstoque()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
