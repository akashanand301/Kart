package com.example.kart.data.repository

import com.example.kart.data.local.dao.CartDao
import com.example.kart.data.mapper.toDomain
import com.example.kart.data.mapper.toEntity
import com.example.kart.domain.model.CartItem
import com.example.kart.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CartRepositoryImpl(
    private val dao: CartDao
) : CartRepository {

    override fun observeCart(): Flow<List<CartItem>> {
        return dao.observeCart().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(item: CartItem) = withContext(Dispatchers.IO) {
        val existingItem = dao.getItemById(item.productId)
        if (existingItem != null) {
            dao.updateItem(existingItem.copy(quantity = existingItem.quantity + 1))
        } else {
            val qty = if (item.quantity > 0) item.quantity else 1
            dao.insertItem(item.toEntity().copy(quantity = qty))
        }
    }

    override suspend fun increaseQuantity(productId: Int) = withContext(Dispatchers.IO) {
        val existingItem = dao.getItemById(productId)
        if (existingItem != null) {
            dao.updateItem(existingItem.copy(quantity = existingItem.quantity + 1))
        }
    }

    override suspend fun decreaseQuantity(productId: Int) = withContext(Dispatchers.IO) {
        val existingItem = dao.getItemById(productId)
        if (existingItem != null) {
            val newQuantity = existingItem.quantity - 1
            if (newQuantity <= 0) {
                dao.deleteItem(productId)
            } else {
                dao.updateItem(existingItem.copy(quantity = newQuantity))
            }
        }
    }

    override suspend fun removeFromCart(productId: Int) = withContext(Dispatchers.IO) {
        dao.deleteItem(productId)
    }

    override suspend fun clearCart() = withContext(Dispatchers.IO) {
        dao.clearCart()
    }
}
