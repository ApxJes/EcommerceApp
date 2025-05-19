package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.domain.model.ProductVo

class PopularProductsPagingSource (
    private val api: ItemsApiService,
): PagingSource<Int, ProductVo>() {

    override fun getRefreshKey(state: PagingState<Int, ProductVo>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductVo> {
        return try {
            val currentPage = params.key ?: 0
            val response = api.getAllProducts(limit = 20, skip = currentPage * params.loadSize)
            val popularProducts = response.products?.map { it!!.toProduct() }?.filter { it.rating!! >= 4.5 }

            LoadResult.Page(
                data = popularProducts!!,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (popularProducts.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}