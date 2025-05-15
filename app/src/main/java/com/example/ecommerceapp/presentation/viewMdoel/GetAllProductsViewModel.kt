package com.example.ecommerceapp.presentation.viewMdoel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.userCase.GetAllProductsUseCase
import com.example.ecommerceapp.domain.userCase.GetProductsByCategoryUseCase
import com.example.ecommerceapp.presentation.state.GetProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetAllProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<GetProductsState> = MutableStateFlow(GetProductsState())
    val state = _state.asStateFlow()

    private val _categoryPagingData = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val categoryPagingData = _categoryPagingData.asStateFlow()

    private var _event: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    val pagedProduct: Flow<PagingData<Product>> =
            getProductsUseCase()
                .cachedIn(viewModelScope)


    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            getProductsByCategoryUseCase(category)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _categoryPagingData.value = pagingData
                }
        }
    }

    sealed class UiEvent {
        data class ToastMessage(val message: String) : UiEvent()
    }
}