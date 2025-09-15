package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Venda
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VendaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val vendasRef = db.collection("vendas")

    // ✅ Registrar nova venda
    suspend fun registrarVenda(venda: Venda) {
        val doc = vendasRef.document()
        val vendaComId = venda.copy(id = doc.id)
        doc.set(vendaComId).await()
    }

    // ✅ Atualizar venda existente
    suspend fun atualizarVenda(venda: Venda) {
        if (venda.id.isNotBlank()) {
            vendasRef.document(venda.id).set(venda).await()
        } else {
            throw IllegalArgumentException("ID da venda não pode ser vazio para atualização")
        }
    }

    // ✅ Obter todas as vendas
    suspend fun obterVendas(): List<Venda> {
        val snapshot = vendasRef.get().await()
        return snapshot.toObjects(Venda::class.java)
    }

    // ✅ Deletar venda
    suspend fun deletarVenda(id: String) {
        if (id.isNotBlank()) {
            vendasRef.document(id).delete().await()
        } else {
            throw IllegalArgumentException("ID da venda não pode ser vazio para exclusão")
        }
    }
}
