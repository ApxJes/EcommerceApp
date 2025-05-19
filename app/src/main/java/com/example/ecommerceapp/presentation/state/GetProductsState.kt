package com.example.ecommerceapp.presentation.state

import com.example.ecommerceapp.domain.model.ProductVo

data class GetProductsState(
    val products: List<ProductVo> = emptyList(),
    val isLoading: Boolean = false
)
