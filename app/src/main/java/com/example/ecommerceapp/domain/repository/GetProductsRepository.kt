package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface GetProductsRepository {
    suspend fun getAllProducts(): ItemsDto

    suspend fun getProductsByCategory(category: String): ItemsDto

    fun getProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product)

    suspend fun deleteProduct(product: Product)
}