package com.example.ecommerceapp.domain.model

data class ItemVo(
    val limit: Int?,
    val produce: List<ProductVo>,
    val skip: Int?,
    val total: Int?
)
