package com.example.ecommerceapp.domain.model

data class Product(
    val availabilityStatus: String?,
    val brand: String?,
    val category: String?,
    val description: String?,
    val discountPercentage: Double?,
    val id: Int?,
    val images: List<String?>?,
    val minimumOrderQuantity: Int?,
    val price: Double?,
    val rating: Double?,
    val reviews: List<Review?>?,
    val stock: Int?,
    val tags: List<String?>?,
    val thumbnail: String?,
    val title: String?,
)
