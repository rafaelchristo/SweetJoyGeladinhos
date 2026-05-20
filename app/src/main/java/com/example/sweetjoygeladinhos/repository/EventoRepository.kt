package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Evento
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val eventosRef = db.collection("eventos")

    suspend fun criarEvento(evento: Evento) {
        eventosRef.add(evento).await()
    }

    suspend fun obterEventos(): List<Evento> {
        val snapshot = eventosRef.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Evento::class.java) }
    }

    suspend fun obterEventoPorId(id: String): Evento? {
        val snapshot = eventosRef.document(id).get().await()
        return snapshot.toObject(Evento::class.java)
    }

    suspend fun deletarEvento(id: String) {
        eventosRef.document(id).delete().await()
    }

    suspend fun atualizarEvento(evento: Evento) {
        eventosRef.document(evento.id).set(evento).await()
    }
}
