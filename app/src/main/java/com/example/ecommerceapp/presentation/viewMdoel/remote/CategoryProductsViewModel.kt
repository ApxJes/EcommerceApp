package com.example.ecommerceapp.presentation.viewMdoel.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _categoryPagingData = MutableStateFlow<PagingData<ProductVo>>(PagingData.empty())
    val categoryPagingData = _categoryPagingData.asStateFlow()

    fun getProductsByCategory(categories: List<String>) {
        viewModelScope.launch {
            getProductsByCategoryUseCase(categories)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _categoryPagingData.value = pagingData
                }
        }
    }
}
