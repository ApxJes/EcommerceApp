package com.example.ecommerceapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.data.local.ProductDao
import com.example.ecommerceapp.data.paging.DIscountPagingSource
import com.example.ecommerceapp.data.paging.PopularProductsPagingSource
import com.example.ecommerceapp.data.paging.ProductByCategoryPagingSource
import com.example.ecommerceapp.data.paging.ProductPagingSource
import com.example.ecommerceapp.data.paging.RecommendProductsPagingSource
import com.example.ecommerceapp.data.paging.SearchingPagingSource
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: ItemsApiService,
    private val dao: ProductDao
) : ProductsRepository {

    override suspend fun getSomeProducts(): ItemsDto {
        return api.getSomeProducts()
    }

    override fun getAllProducts(): Flow<PagingData<ProductVo>> {
        return Pager(
            PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { ProductPagingSource(api) }
        ).flow
    }

    override suspend fun getProductDetailsById(id: Int): ProductVo {
        return api.getProductDetailsById(id).toProduct()
    }

    override fun getProductsByCategory(category: List<String>): Pager<Int, ProductVo> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductByCategoryPagingSource(api = api, categories = category)}
        )
    }

    override fun searchProducts(query: String): Pager<Int, ProductVo> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchingPagingSource(api = api, searchQuery = query) }
        )
    }

    override fun getPopularProducts(): Pager<Int, ProductVo> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularProductsPagingSource(api = api)}
        )
    }

    override fun getRecommendProducts(): Pager<Int, ProductVo> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecommendProductsPagingSource(api = api)}
        )
    }

    override fun getDiscountsProducts(category: String): Pager<Int, ProductVo> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                DIscountPagingSource(
                    api = api,
                    category = category,
                    applyFifteenPercentDiscount = true
                )
            }
        )
    }

    override fun getProducts(): Flow<List<ProductVo>> {
        return dao.getProducts()
    }

    override suspend fun insertProduct(product: ProductVo) = dao.insertProduct(product)


    override suspend fun deleteProduct(product: ProductVo) = dao.deleteProduct(product)

}