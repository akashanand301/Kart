package com.example.kart.data.mapper

import com.example.kart.data.remote.dto.ProductDto
import com.example.kart.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        category = category,
        brand = brand ?: "",
        stock = stock,
        thumbnail = thumbnail,
        images = images
    )
}
