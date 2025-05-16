package com.example.ecommerceapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.ecommerceapp.data.paging.ProductByCategoryPagingSource
import com.example.ecommerceapp.data.paging.ProductPagingSource
import com.example.ecommerceapp.data.local.ProductDao
import com.example.ecommerceapp.data.paging.PopularProductsPagingSource
import com.example.ecommerceapp.data.paging.RecommendProductsPagingSource
import com.example.ecommerceapp.data.paging.SearchingPagingSource
import com.example.ecommerceapp.data.remote.ItemsApi
import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.repository.GetProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(
    private val api: ItemsApi,
    private val dao: ProductDao
) : GetProductsRepository {

    override fun getAllProducts(): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(api) }
        )
    }

    override fun getProductsByCategory(category: String): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductByCategoryPagingSource(api = api, category = category)}
        )
    }

    override fun searchProducts(query: String): Pager<Int, Product> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchingPagingSource(api = api, searchQuery = query) }
        )
    }

    override fun getPopularProducts(): Pager<Int, Product> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularProductsPagingSource(api = api)}
        )
    }

    override fun getRecommendProducts(): Pager<Int, Product> {
        return Pager (
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecommendProductsPagingSource(api = api)}
        )
    }


    override fun getProducts(): Flow<List<Product>> {
        return dao.getProducts()
    }

    override suspend fun insertProduct(product: Product) = dao.insertProduct(product)


    override suspend fun deleteProduct(product: Product) = dao.deleteProduct(product)

}