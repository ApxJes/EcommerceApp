package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetSomeProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
){

    operator fun invoke(): Flow<Resource<List<ProductVo>>> = flow {
        try {
            emit(Resource.Loading())
            val products = repository.getSomeProducts().products!!.map { it!!.toProduct() }
            emit(Resource.Success(products))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Couldn't reach server please try again"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Oops! Something went wrong"))
        }
    }
}