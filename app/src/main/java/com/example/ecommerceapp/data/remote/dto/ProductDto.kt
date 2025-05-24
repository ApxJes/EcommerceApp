package com.example.ecommerceapp.data.remote.dto


import com.example.ecommerceapp.domain.model.ProductVo
import com.google.gson.annotations.SerializedName
import kotlin.String
import kotlin.collections.List

data class ProductDto(
    @SerializedName("availabilityStatus")
    val availabilityStatus: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("dimensions")
    val dimensions: DimensionsDto?,
    @SerializedName("discountPercentage")
    val discountPercentage: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("images")
    val images: List<String?>?,
    @SerializedName("meta")
    val meta: MetaDto?,
    @SerializedName("minimumOrderQuantity")
    val minimumOrderQuantity: Int?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("returnPolicy")
    val returnPolicy: String?,
    @SerializedName("reviews")
    val reviews: List<ReviewDto?>?,
    @SerializedName("shippingInformation")
    val shippingInformation: String?,
    @SerializedName("sku")
    val sku: String?,
    @SerializedName("stock")
    val stock: Int?,
    @SerializedName("tags")
    val tags: List<String?>?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("warrantyInformation")
    val warrantyInformation: String?,
    @SerializedName("weight")
    val weight: Int?
) {
    fun toProduct(): ProductVo {
        return ProductVo (
            availabilityStatus = availabilityStatus,
            brand = brand,
            category = category,
            description = description,
            discountPercentage = discountPercentage,
            id = id,
            minimumOrderQuantity = minimumOrderQuantity,
            rating = rating,
            price = price,
            reviews = reviews!!.map { it!!.toReview() },
            stock = stock,
            thumbnail = thumbnail,
            title = title,
            warrantyInformation = warrantyInformation,
            shippingInformation = shippingInformation,
            images =  images,
        )
    }
}