package com.example.ecommerceapp.data.remote.dto


import com.example.ecommerceapp.domain.model.ItemVo
import com.google.gson.annotations.SerializedName

data class ItemsDto(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("products")
    val products: List<ProductDto?>?,
    @SerializedName("skip")
    val skip: Int?,
    @SerializedName("total")
    val total: Int?
)