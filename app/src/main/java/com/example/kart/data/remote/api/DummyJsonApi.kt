package com.example.kart.data.remote.api

import com.example.kart.data.remote.dto.ProductDto
import com.example.kart.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DummyJsonApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductsResponseDto

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): ProductsResponseDto
}
