package com.example.ecommerceapp.data.remote

import com.example.ecommerceapp.data.remote.dto.ItemsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemsApi {

    @GET("products")
    suspend fun getItems(): ItemsDto

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") category: String
    ): ItemsDto
}