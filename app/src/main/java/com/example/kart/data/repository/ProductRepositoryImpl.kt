package com.example.kart.data.repository

import com.example.kart.core.common.Result
import com.example.kart.data.mapper.toDomain
import com.example.kart.data.remote.api.DummyJsonApi
import com.example.kart.domain.model.Product
import com.example.kart.domain.repository.ProductRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ProductRepositoryImpl(
    private val api: DummyJsonApi
) : ProductRepository {

    override suspend fun getProducts(limit: Int, skip: Int): Result<List<Product>> {
        return try {
            val response = api.getProducts(limit, skip)
            Result.Success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(mapExceptionToMessage(e), e)
        }
    }

    override suspend fun getProduct(id: Int): Result<Product> {
        return try {
            val response = api.getProduct(id)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(mapExceptionToMessage(e), e)
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = api.searchProducts(query)
            Result.Success(response.products.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(mapExceptionToMessage(e), e)
        }
    }

    private fun mapExceptionToMessage(e: Exception): String {
        return when (e) {
            is UnknownHostException -> "No internet connection. Please check your connection."
            is SocketTimeoutException -> "Request timed out. Please try again."
            is IOException -> "Network error. Please check your connection."
            is HttpException -> "Unable to load products. Please try again."
            else -> "Something went wrong. Please try again."
        }
    }
}
