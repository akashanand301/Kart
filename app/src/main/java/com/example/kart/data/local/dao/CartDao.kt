package com.example.kart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kart.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun observeCart(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: CartEntity)

    @Update
    fun updateItem(item: CartEntity)

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    fun getItemById(productId: Int): CartEntity?

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    fun deleteItem(productId: Int)

    @Query("DELETE FROM cart_items")
    fun clearCart()
}
