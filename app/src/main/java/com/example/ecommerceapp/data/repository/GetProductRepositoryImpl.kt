package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.ItemsApi
import com.example.ecommerceapp.data.dto.ItemsDto
import com.example.ecommerceapp.data.dto.ProductDto
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(
    private val api: ItemsApi
): GetProductsRepository {
    override suspend fun getProducts(): ItemsDto {
        return api.getItems()
    }

    override suspend fun getProductsByCategory(category: String): ItemsDto {
        return api.getProductsByCategory(category)
    }
}