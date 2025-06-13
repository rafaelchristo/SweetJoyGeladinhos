package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Receita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReceitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val receitasRef = db.collection("receitas")

    suspend fun adicionarReceita(receita: Receita) {
        val doc = receitasRef.document()
        val receitaComId = receita.copy(id = doc.id)
        doc.set(receitaComId).await()
    }

    suspend fun obterReceitas(): List<Receita> {
        val snapshot = receitasRef.get().await()
        return snapshot.toObjects(Receita::class.java)
    }

    suspend fun deletarReceita(id: String) {
        receitasRef.document(id).delete().await()
    }
}
