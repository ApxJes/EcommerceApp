package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.domain.model.ProductVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(): ViewModel(){

    private val _cartItems = MutableStateFlow<List<ProductVo>>(emptyList())
    val cartItem get() = _cartItems.asStateFlow()

    fun addToCart(product: ProductVo) {
        _cartItems.value = _cartItems.value + product
    }

    fun removeCart(product: ProductVo) {
        _cartItems.value = _cartItems.value.filter { it != product }
    }
}