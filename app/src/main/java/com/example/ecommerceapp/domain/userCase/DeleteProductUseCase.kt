package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: GetProductsRepository
) {

    suspend operator fun invoke(product: ProductVo) {
        repository.deleteProduct(product)
    }
}