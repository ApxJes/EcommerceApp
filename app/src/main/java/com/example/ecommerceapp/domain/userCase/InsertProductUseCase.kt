package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import javax.inject.Inject

class InsertProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend  operator fun invoke(product: ProductVo) {
        repository.insertProduct(product)
    }
}