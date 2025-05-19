package com.example.ecommerceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerceapp.data.remote.api_service.ItemsApiService
import com.example.ecommerceapp.domain.model.ProductVo

class DIscountPagingSource (
    private val api: ItemsApiService,
    private val category: String,
    private val applyFifteenPercentDiscount: Boolean
): PagingSource<Int, ProductVo>() {

    override fun getRefreshKey(state: PagingState<Int, ProductVo>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductVo> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val response = api.getProductsByCategory(
                category = category,
                limit = 20,
                skip = page * pageSize
            )

            val filteredItems: List<ProductVo> = if (applyFifteenPercentDiscount) {
                response.products
                    ?.mapNotNull { it?.toProduct() }
                    ?.filter {
                        val discount = it.discountPercentage ?: 0.0
                        discount >1.0 && discount < 15.0
                    } ?: emptyList()
            } else {
                response.products?.mapNotNull { it?.toProduct() } ?: emptyList()
            }

            LoadResult.Page(
                data = filteredItems,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (filteredItems.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}