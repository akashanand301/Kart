package com.example.kart.core.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kart.KartApplication
import com.example.kart.feature.cart.CartViewModel
import com.example.kart.feature.product.details.ProductDetailsViewModel
import com.example.kart.feature.product.list.ProductListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ProductListViewModel(
                kartApplication().container.productRepository
            )
        }
        initializer {
            ProductDetailsViewModel(
                kartApplication().container.productRepository,
                kartApplication().container.cartRepository
            )
        }
        initializer {
            CartViewModel(
                kartApplication().container.cartRepository
            )
        }
    }
}

fun CreationExtras.kartApplication(): KartApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KartApplication)
