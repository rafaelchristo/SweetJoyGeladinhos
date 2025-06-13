package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Promocao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PromocaoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val promocoesRef = db.collection("promocoes")

    suspend fun adicionarPromocao(promocao: Promocao) {
        val doc = promocoesRef.document()
        val promocaoComId = promocao.copy(id = doc.id)
        doc.set(promocaoComId).await()
    }

    suspend fun atualizarPromocao(promocao: Promocao) {
        if (promocao.id.isNotBlank()) {
            promocoesRef.document(promocao.id).set(promocao).await()
        }
    }

    suspend fun obterPromocoes(): List<Promocao> {
        val snapshot = promocoesRef.get().await()
        return snapshot.toObjects(Promocao::class.java)
    }

    suspend fun deletarPromocao(id: String) {
        promocoesRef.document(id).delete().await()
    }
}
