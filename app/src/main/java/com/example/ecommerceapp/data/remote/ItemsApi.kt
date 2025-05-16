package com.example.ecommerceapp.data.remote

import com.example.ecommerceapp.data.remote.dto.ItemsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemsApi {

    @GET("products")
    suspend fun getItems(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ItemsDto

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ItemsDto

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ItemsDto
}