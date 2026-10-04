package com.example.kart.domain.repository

import com.example.kart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartItem>>
    suspend fun addToCart(item: CartItem)
    suspend fun increaseQuantity(productId: Int)
    suspend fun decreaseQuantity(productId: Int)
    suspend fun removeFromCart(productId: Int)
    suspend fun clearCart()
}
