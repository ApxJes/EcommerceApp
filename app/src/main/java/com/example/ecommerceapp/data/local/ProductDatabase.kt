package com.example.ecommerceapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceapp.domain.model.ProductVo

@Database(entities = [ProductVo::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
}