package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(): Flow<List<ProductVo>> {
        return repository.getProducts()
    }
}