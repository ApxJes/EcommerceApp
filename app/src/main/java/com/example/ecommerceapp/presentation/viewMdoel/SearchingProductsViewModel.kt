package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.SearchingProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingProductsViewModel @Inject constructor(
    private val searchingProductsUseCase: SearchingProductsUseCase
): ViewModel() {

    private val _searchResult = MutableStateFlow<PagingData<ProductVo>>(PagingData.empty())
    val searchResult = _searchResult.asStateFlow()

    fun searchingProducts(query: String) {
        viewModelScope.launch {
            searchingProductsUseCase(query)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _searchResult.value = pagingData
                }
        }
    }
}