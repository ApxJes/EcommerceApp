package com.example.ecommerceapp.domain.repository

import androidx.paging.Pager
import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.ProductVo
import kotlinx.coroutines.flow.Flow

interface GetProductsRepository {

    suspend fun getSomeProducts(): ItemsDto

    fun getAllProducts(): Pager<Int, ProductVo>

    fun getProductsByCategory(category: String): Pager<Int, ProductVo>

    fun searchProducts(query: String): Pager<Int, ProductVo>

    fun getPopularProducts(): Pager<Int, ProductVo>

    fun getRecommendProducts(): Pager<Int, ProductVo>

    fun getDiscountsProducts(category: String): Pager<Int, ProductVo>

    fun getProducts(): Flow<List<ProductVo>>

    suspend fun insertProduct(product: ProductVo)

    suspend fun deleteProduct(product: ProductVo)
}