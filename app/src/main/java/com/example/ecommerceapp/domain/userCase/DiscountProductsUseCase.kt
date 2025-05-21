package com.example.ecommerceapp.domain.userCase

import androidx.paging.PagingData
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscountProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    operator fun invoke(category: String): Flow<PagingData<ProductVo>> {
        return repository.getDiscountsProducts(category).flow
    }
}
