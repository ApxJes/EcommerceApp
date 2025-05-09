package com.example.ecommerceapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceapp.domain.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao
}