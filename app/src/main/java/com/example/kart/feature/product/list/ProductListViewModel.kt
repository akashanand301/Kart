package com.example.kart.feature.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kart.core.common.Result
import com.example.kart.core.common.UiState
import com.example.kart.domain.model.Product
import com.example.kart.domain.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null
    
    private var currentPage = 0
    private var isLastPage = false

    init {
        loadProducts()
    }

    fun loadProducts(isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 0
            isLastPage = false
        }
        if (isLastPage) return

        viewModelScope.launch {
            if (currentPage == 0) _uiState.value = UiState.Loading
            
            when (val result = repository.getProducts(limit = 20, skip = currentPage * 20)) {
                is Result.Success -> {
                    val currentProducts = if (currentPage == 0) emptyList() else (_uiState.value as? UiState.Success)?.data ?: emptyList()
                    val newProducts = result.data
                    if (newProducts.size < 20) isLastPage = true
                    _uiState.value = UiState.Success(currentProducts + newProducts)
                    currentPage++
                }
                is Result.Error -> if (currentPage == 0) _uiState.value = UiState.Error(result.message)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            if (query.isBlank()) {
                loadProducts(isRefresh = true)
            } else {
                _uiState.value = UiState.Loading
                when (val result = repository.searchProducts(query)) {
                    is Result.Success -> _uiState.value = UiState.Success(result.data)
                    is Result.Error -> _uiState.value = UiState.Error(result.message)
                }
            }
        }
    }
}
