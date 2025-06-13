package com.example.sweetjoygeladinhos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sweetjoygeladinhos.repository.PromocaoRepository

class PromocaoViewModelFactory(
    private val repository: PromocaoRepository = PromocaoRepository()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PromocaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PromocaoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}