package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Receita
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreReceitaRepository {

    private val db = FirebaseFirestore.getInstance()
    private val receitaCollection = db.collection("receitas")

    suspend fun getReceitas(): List<Receita> {
        val snapshot = receitaCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Receita::class.java)?.copy(id = it.id)
        }
    }

    suspend fun addReceita(receita: Receita) {
        receitaCollection.add(receita).await()
    }

    suspend fun updateReceita(receita: Receita) {
        receitaCollection.document(receita.id).set(receita).await()
    }

    suspend fun deleteReceita(id: String) {
        receitaCollection.document(id).delete().await()
    }
}
