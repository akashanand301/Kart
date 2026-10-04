package com.example.kart.data.mapper

import com.example.kart.data.local.entity.CartEntity
import com.example.kart.domain.model.CartItem

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        productId = productId,
        title = title,
        price = price,
        thumbnail = thumbnail,
        quantity = quantity
    )
}

fun CartItem.toEntity(): CartEntity {
    return CartEntity(
        productId = productId,
        title = title,
        price = price,
        thumbnail = thumbnail,
        quantity = quantity
    )
}
