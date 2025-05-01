package com.example.ecommerceapp.domain.model

data class Item(
    val limit: Int?,
    val produce: List<Product>,
    val skip: Int?,
    val total: Int?
)
