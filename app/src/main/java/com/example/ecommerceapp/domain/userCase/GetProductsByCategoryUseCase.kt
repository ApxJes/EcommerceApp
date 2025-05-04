package com.example.ecommerceapp.domain.userCase

import android.util.Log
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: GetProductsRepository
) {

    operator fun invoke(category: String): Flow<Resource<List<Product>>> = flow {
        Log.d("GetProductsByCategory", "Fetching products for category: $category")
        try {
            emit(Resource.Loading())
            val productByCategory = repository.getProductsByCategory(category).products!!.map { it!!.toProduct() }
            emit(Resource.Success(productByCategory))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error occur!"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Couldn't reach server, please check your internet connection!!"))
        }
    }
}