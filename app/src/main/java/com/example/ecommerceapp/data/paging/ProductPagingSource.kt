package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.ItemsApi
import com.example.ecommerceapp.domain.model.Product

class ProductPagingSource(
    private val api: ItemsApi
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition) ?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val response = api.getItems(
                limit = 10,
                skip = page * pageSize
            )

            val product = response.products?.map { it!!.toProduct() } ?: emptyList()

            LoadResult.Page (
                data = product,
                prevKey = if(page == 0) null else page - 1,
                nextKey = if(product.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}