package com.example.ecommerceapp.presentation.viewMdoel.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetRecommendProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecommendedProductsViewModel @Inject constructor(
    getRecommendProductsUseCase: GetRecommendProductsUseCase
) : ViewModel() {

    val recommendedProducts: Flow<PagingData<ProductVo>> =
        getRecommendProductsUseCase()
            .cachedIn(viewModelScope)
}
