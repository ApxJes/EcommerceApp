package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: GetProductsRepository
) {

    operator fun invoke(): Flow<List<ProductVo>> {
        val products = repository.getProducts()
        return products
    }
}