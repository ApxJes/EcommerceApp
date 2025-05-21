package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.domain.model.ProductVo

class ProductByCategoryPagingSource(
    private val api: ItemsApiService,
    private val categories: List<String>
) : PagingSource<Int, ProductVo>() {
    override fun getRefreshKey(state: PagingState<Int, ProductVo>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductVo> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val allProducts = mutableListOf<ProductVo>()
            for (category in categories) {
                val response = api.getProductsByCategory(
                    category,
                    limit = 20,
                    skip = page * pageSize
                )

                val products = response.products?.mapNotNull { it?.toProduct() } ?: emptyList()
                allProducts.addAll(products)
            }

            LoadResult.Page(
                data = allProducts,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (allProducts.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}