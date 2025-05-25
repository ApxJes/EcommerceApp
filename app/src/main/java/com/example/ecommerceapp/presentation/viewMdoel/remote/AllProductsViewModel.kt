package com.example.ecommerceapp.presentation.viewMdoel.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _allProducts = MutableStateFlow<PagingData<ProductVo>>(PagingData.empty())
    val allProducts: StateFlow<PagingData<ProductVo>> = _allProducts.asStateFlow()

    init {
        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _allProducts.value = pagingData
                }
        }
    }
}
