package com.example.kart.core.network

import android.content.Context
import androidx.room.Room
import com.example.kart.data.local.AppDatabase
import com.example.kart.data.remote.api.DummyJsonApi
import com.example.kart.data.repository.CartRepositoryImpl
import com.example.kart.data.repository.ProductRepositoryImpl
import com.example.kart.domain.repository.CartRepository
import com.example.kart.domain.repository.ProductRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl = "https://dummyjson.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val dummyJsonApi: DummyJsonApi by lazy {
        retrofit.create(DummyJsonApi::class.java)
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "kart_database"
        ).build()
    }

    override val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(dummyJsonApi)
    }

    override val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(appDatabase.cartDao())
    }
}
