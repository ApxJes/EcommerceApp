package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.domain.model.ProductVo

class SearchingPagingSource (
    private val api: ItemsApiService,
    private val searchQuery: String
): PagingSource<Int, ProductVo>() {
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

            val response = api.searchProducts(
                limit = 10,
                skip = page * pageSize,
                query = searchQuery
            )

            val products = response.products?.map { it!!.toProduct() } ?: emptyList()

            LoadResult.Page(
                data = products,
                prevKey = if(page == 0 ) null else page - 1,
                nextKey = if(products.isEmpty()) null else page + 1
            )

        } catch (e: Exception){
            LoadResult.Error(e)
        }

    }
}