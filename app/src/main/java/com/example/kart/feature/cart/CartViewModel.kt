package com.example.kart.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kart.domain.model.CartItem
import com.example.kart.domain.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalCount: Int = 0,
    val totalPrice: Double = 0.0
)

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartUiState: StateFlow<CartUiState> = cartRepository.observeCart()
        .map { items ->
            val totalCount = items.sumOf { it.quantity }
            val totalPrice = items.sumOf { it.price * it.quantity }
            CartUiState(items, totalCount, totalPrice)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState()
        )

    fun increaseQuantity(productId: Int) {
        viewModelScope.launch {
            cartRepository.increaseQuantity(productId)
        }
    }

    fun decreaseQuantity(productId: Int) {
        viewModelScope.launch {
            cartRepository.decreaseQuantity(productId)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }
}
