package com.example.ecommerceapp.domain.userCase

import androidx.paging.PagingData
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(category: List<String>): Flow<PagingData<ProductVo>> {
        return repository.getProductsByCategory(category).flow
    }
}