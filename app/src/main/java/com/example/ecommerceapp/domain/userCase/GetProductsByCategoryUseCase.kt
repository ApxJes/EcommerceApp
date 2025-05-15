package com.example.ecommerceapp.domain.userCase

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.data.paging.ProductByCategoryPagingSource
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

    operator fun invoke(category: String): Flow<PagingData<Product>> {
        return repository.getProductsByCategory(category).flow
    }

}