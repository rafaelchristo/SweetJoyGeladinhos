import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetjoygeladinhos.model.*
import com.example.sweetjoygeladinhos.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstoqueComProdutoViewModel : ViewModel() {

    private val estoqueRepo = FirestoreEstoqueRepository()
    private val produtoRepo = FirestoreProdutoRepository()

    private val _estoqueComProdutos = MutableStateFlow<List<EstoqueItemComProduto>>(emptyList())
    val estoqueComProdutos: StateFlow<List<EstoqueItemComProduto>> get() = _estoqueComProdutos

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> get() = _erro

    fun carregarEstoqueComProdutos() {
        viewModelScope.launch {
            try {
                val estoqueItems = estoqueRepo.getEstoque()
                val produtos = produtoRepo.getProdutos()

                val listaCombinada = estoqueItems.mapNotNull { item ->
                    val produto = produtos.find { it.produtoId == item.produtoId }
                    produto?.let {
                        EstoqueItemComProduto(item = item, produto = it)
                    }
                }

                _estoqueComProdutos.value = listaCombinada

            } catch (e: Exception) {
                _erro.value = e.message
            }
        }
    }
}
