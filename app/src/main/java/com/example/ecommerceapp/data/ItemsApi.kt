package com.example.ecommerceapp.data

import com.example.ecommerceapp.data.dto.ItemsDto
import retrofit2.http.GET

interface ItemsApi {

    @GET("products")
    suspend fun getItems(): ItemsDto
}