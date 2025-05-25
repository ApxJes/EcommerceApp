package com.example.ecommerceapp.presentation.viewMdoel.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetPopularProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularProductsViewModel @Inject constructor(
    getPopularProductsUseCase: GetPopularProductsUseCase
) : ViewModel() {

    val popularProducts: Flow<PagingData<ProductVo>> =
        getPopularProductsUseCase()
            .cachedIn(viewModelScope)
}
