package com.example.kart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kart.data.local.dao.CartDao
import com.example.kart.data.local.entity.CartEntity

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
