package com.example.ecommerceapp.domain.repository

import com.example.ecommerceapp.data.dto.ItemsDto
import com.example.ecommerceapp.data.dto.ProductDto

interface GetProductsRepository {

    suspend fun getProducts(): ItemsDto
}