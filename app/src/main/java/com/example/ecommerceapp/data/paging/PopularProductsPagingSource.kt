package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.ItemsApi
import com.example.ecommerceapp.domain.model.Product

class PopularProductsPagingSource (
    private val api: ItemsApi,
): PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 0
            val response = api.getItems(limit = 20, skip = currentPage * params.loadSize)
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