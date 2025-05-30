package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreVendaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val vendasCollection = db.collection("vendas")

    suspend fun getVendas(): List<Venda> {
        val snapshot = vendasCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Venda::class.java)?.copy(id = it.id)
        }
    }

    suspend fun addVenda(venda: Venda) {
        vendasCollection.add(venda).await()
    }

    suspend fun updateVenda(venda: Venda) {
        vendasCollection.document(venda.id).set(venda).await()
    }

    suspend fun deleteVenda(id: String) {
        vendasCollection.document(id).delete().await()
    }
}
