package com.example.kart.feature.product.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kart.core.common.Result
import com.example.kart.core.common.UiState
import com.example.kart.domain.model.CartItem
import com.example.kart.domain.model.Product
import com.example.kart.domain.repository.CartRepository
import com.example.kart.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val uiState: StateFlow<UiState<Product>> = _uiState.asStateFlow()

    private val _addedToCart = MutableStateFlow(false)
    val addedToCart: StateFlow<Boolean> = _addedToCart.asStateFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = productRepository.getProduct(id)) {
                is Result.Success -> _uiState.value = UiState.Success(result.data)
                is Result.Error -> _uiState.value = UiState.Error(result.message)
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val item = CartItem(
                productId = product.id,
                title = product.title,
                price = product.price,
                thumbnail = product.thumbnail,
                quantity = 1
            )
            cartRepository.addToCart(item)
            _addedToCart.value = true
        }
    }

    fun resetAddedToCart() {
        _addedToCart.value = false
    }
}
