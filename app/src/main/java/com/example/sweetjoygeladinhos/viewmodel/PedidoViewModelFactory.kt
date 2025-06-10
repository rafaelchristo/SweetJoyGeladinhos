package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sweetjoygeladinhos.data.EstoqueDao
import com.example.sweetjoygeladinhos.data.PedidoDao
import com.example.sweetjoygeladinhos.data.ProdutoDao
import com.example.sweetjoygeladinhos.data.VendaDao

class PedidosViewModelFactory(
    private val produtoDao: ProdutoDao,
    private val pedidoDao: PedidoDao,
    private val estoqueDao: EstoqueDao,
    private val vendaDao: VendaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PedidosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PedidosViewModel(produtoDao, pedidoDao, estoqueDao, vendaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
