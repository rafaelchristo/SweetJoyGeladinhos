package com.example.sweetjoygeladinhos.repository

import com.example.sweetjoygeladinhos.model.Pedido
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PedidoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val pedidosRef = db.collection("pedidos")

    suspend fun adicionarPedido(pedido: Pedido) {
        val novoDoc = pedidosRef.document()
        val pedidoComId = pedido.copy(id = novoDoc.id)  // usa 'id', como no seu modelo
        novoDoc.set(pedidoComId).await()
    }

    suspend fun obterPedidos(): List<Pedido> {
        val snapshot = pedidosRef.get().await()
        return snapshot.toObjects(Pedido::class.java)
    }

    suspend fun deletarPedido(id: String) {
        pedidosRef.document(id).delete().await()
    }
}
