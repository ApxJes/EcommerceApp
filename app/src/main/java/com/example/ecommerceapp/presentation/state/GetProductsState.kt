package com.example.ecommerceapp.presentation.state

import com.example.ecommerceapp.domain.model.Product

data class GetProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false
)
