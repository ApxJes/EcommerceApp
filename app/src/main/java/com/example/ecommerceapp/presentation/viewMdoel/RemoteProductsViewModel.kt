package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.model.ProductVo
import com.example.ecommerceapp.domain.userCase.GetAllProductsUseCase
import com.example.ecommerceapp.domain.userCase.GetPopularProductsUseCase
import com.example.ecommerceapp.domain.userCase.GetProductsByCategoryUseCase
import com.example.ecommerceapp.domain.userCase.GetRecommendProductsUseCase
import com.example.ecommerceapp.domain.userCase.GetSomeProductsUseCase
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
class RemoteProductsViewModel @Inject constructor(
    private val getSomeProductsUseCase: GetSomeProductsUseCase,
    getProductsUseCase: GetAllProductsUseCase,
    getPopularProductsUseCase: GetPopularProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    getRecommendProductsUseCase: GetRecommendProductsUseCase,
) : ViewModel() {

    private var _state: MutableStateFlow<GetProductsState> = MutableStateFlow(GetProductsState())
    val state = _state.asStateFlow()

    private val _categoryPagingData = MutableStateFlow<PagingData<ProductVo>>(PagingData.empty())
    val categoryPagingData = _categoryPagingData.asStateFlow()

    private var _event: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private var _getSomeProducts: MutableStateFlow<GetProductsState> =
        MutableStateFlow((GetProductsState()))
    val getSomeProducts = _getSomeProducts.asStateFlow()

    init {
        getSomeProducts()
    }

    val getAllAvailableProducts: Flow<PagingData<ProductVo>> =
            getProductsUseCase()
                .buffer()
                .cachedIn(viewModelScope)

    val popularProducts: Flow<PagingData<ProductVo>> =
        getPopularProductsUseCase()
            .cachedIn(viewModelScope)

    val recommendProducts: Flow<PagingData<ProductVo>> =
        getRecommendProductsUseCase()
            .cachedIn(viewModelScope)

    fun getSomeProducts() {
        viewModelScope.launch {
            getSomeProductsUseCase()
                .buffer()
                .onEach { products ->
                    when (products) {
                        is Resource.Success -> {
                            _getSomeProducts.value = _getSomeProducts.value.copy(
                                products.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _getSomeProducts.value = _getSomeProducts.value.copy(
                                isLoading = false
                            )

                            _event.emit(
                                UiEvent.ToastMessage(products.message ?: "Unknown message")
                            )
                        }

                        is Resource.Loading -> {
                            _getSomeProducts.value = _getSomeProducts.value.copy(isLoading = true)
                        }
                    }
                }
                .collect()
        }
    }

    fun getProductsByCategory(category: List<String>) {
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