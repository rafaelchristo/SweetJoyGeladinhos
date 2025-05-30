package com.example.sweetjoygeladinhos.data

import com.example.sweetjoygeladinhos.model.Promocao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestorePromocaoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val promocaoCollection = db.collection("promocoes")

    suspend fun getPromocoes(): List<Promocao> {
        val snapshot = promocaoCollection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Promocao::class.java)?.copy(id = it.id)
        }
    }

    suspend fun addPromocao(promocao: Promocao) {
        promocaoCollection.add(promocao).await()
    }

    suspend fun updatePromocao(promocao: Promocao) {
        promocaoCollection.document(promocao.id).set(promocao).await()
    }

    suspend fun deletePromocao(id: String) {
        promocaoCollection.document(id).delete().await()
    }
}
