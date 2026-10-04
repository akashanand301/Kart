package com.example.kart.domain.repository

import com.example.kart.core.common.Result
import com.example.kart.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProduct(id: Int): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>
}
