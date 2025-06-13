package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VendaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val vendasRef = db.collection("vendas")

    suspend fun registrarVenda(venda: Venda) {
        val doc = vendasRef.document()
        val vendaComId = venda.copy(id = doc.id)
        doc.set(vendaComId).await()
    }

    suspend fun obterVendas(): List<Venda> {
        val snapshot = vendasRef.get().await()
        return snapshot.toObjects(Venda::class.java)
    }

    suspend fun deletarVenda(id: String) {
        vendasRef.document(id).delete().await()
    }
}
