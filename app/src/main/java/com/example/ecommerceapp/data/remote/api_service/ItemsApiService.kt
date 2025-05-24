package com.example.ecommerceapp.data.remote.api_service

import com.example.ecommerceapp.data.remote.dto.ItemsDto
import com.example.ecommerceapp.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItemsApiService {

    @GET("products")
    suspend fun getSomeProducts(): ItemsDto

    @GET("products")
    suspend fun getAllProducts(
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

    @GET("products/{id}")
    suspend fun getProductDetailsById(
        @Path("id") id: Int
    ): ProductDto
}