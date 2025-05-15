package com.example.ecommerceapp.domain.repository

import androidx.paging.Pager
import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface GetProductsRepository {
    fun getAllProducts(): Pager<Int, Product>

    fun getProductsByCategory(category: String): Pager<Int, Product>

    fun getProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product)

    suspend fun deleteProduct(product: Product)
}