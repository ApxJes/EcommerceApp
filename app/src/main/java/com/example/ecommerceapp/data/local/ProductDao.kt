package com.example.ecommerceapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerceapp.domain.model.ProductVo
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products_table")
    fun getProducts(): Flow<List<ProductVo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductVo)

    @Delete
    suspend fun deleteProduct(product: ProductVo)
}