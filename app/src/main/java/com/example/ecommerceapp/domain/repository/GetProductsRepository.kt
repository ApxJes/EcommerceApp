package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.data.dto.ItemsDto

interface GetProductsRepository {
    suspend fun getProducts(): ItemsDto
}