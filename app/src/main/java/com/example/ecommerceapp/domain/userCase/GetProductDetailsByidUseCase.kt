package com.example.ecommerceapp.domain.userCase


import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductDetailsByidUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(id: Int): Flow<Resource<ProductVo>> = flow {
        emit(Resource.Loading())
        try {
            val product = repository.getProductDetailsById(id)
            emit(Resource.Success(product))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}