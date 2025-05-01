package com.example.ecommerceapp.domain.userCase

import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.Item
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: GetProductsRepository
){

    operator fun invoke(): Flow<Resource<List<Product>>> = flow {

        try {
            emit(Resource.Loading())
            val products = repository.getProducts().products!!.map { it!!.toProduct() }
            emit(Resource.Success(products))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error occur!"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Couldn't reach server, please check your internet connection!!"))
        }
    }
}