package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetProductDetailsByidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsByidUseCase
): ViewModel() {

    private val _state =
        MutableStateFlow<Resource<ProductVo>>(Resource.Loading())
    val state = _state.asStateFlow()

    fun fetchProductDetails(id: Int) {
        viewModelScope.launch {
            getProductDetailsUseCase(id).collect {
                _state.value = it
            }
        }
    }
}