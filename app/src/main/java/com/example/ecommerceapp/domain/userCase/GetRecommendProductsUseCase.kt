package com.example.ecommerceapp.domain.userCase

import androidx.paging.PagingData
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecommendProductsUseCase @Inject constructor(
    private val repository: GetProductsRepository
) {

    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getRecommendProducts().flow
    }
}