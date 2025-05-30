package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.domain.model.ProductVo

class RecommendProductsPagingSource (
    private val api: ItemsApiService,
) : PagingSource<Int, ProductVo>() {

    override fun getRefreshKey(state: PagingState<Int, ProductVo>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductVo> {
        return try {
            val page = params.key ?: 0
            val response = api.getAllProducts(limit = 20, skip = page * params.loadSize)

            val recommended = response.products
                ?.mapNotNull { it?.toProduct() }
                ?.filter {
                    (it.rating ?: 0.0) >= 3.0 && (it.discountPercentage ?: 0.0) >= 15
                } ?: emptyList()

            LoadResult.Page(
                data = recommended,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (recommended.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
