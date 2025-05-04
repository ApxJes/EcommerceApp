package com.example.ecommerceapp.presentation.viewMdoel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.userCase.GetProductsByCategoryUseCase
import com.example.ecommerceapp.domain.userCase.GetProductsUseCase
import com.example.ecommerceapp.presentation.state.GetProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private var _state: MutableStateFlow<GetProductsState> = MutableStateFlow(GetProductsState())
    val state = _state.asStateFlow()

    private var _event: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                .buffer()
                .onEach { products ->
                    when (products) {
                        is Resource.Success -> {
                            _state.value = _state.value.copy(
                                products.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false
                            )

                            _event.emit(
                                UiEvent.ToastMessage(
                                    products.message ?: "Unknown error"
                                )
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = _state.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
                .collect()
        }
    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            Log.d("GetProductsViewModel", "getProductsByCategory: Fetching products for category: $category")
            getProductsByCategoryUseCase(category)
                .buffer()
                .onEach { products ->
                    when (products) {
                        is Resource.Success -> {
                            Log.d("GetProductsViewModel", "getProductsByCategory: Success - Received ${products.data?.size ?: 0} products")
                            _state.value = _state.value.copy(
                                products = products.data ?: emptyList(),
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            Log.e("GetProductsViewModel", "getProductsByCategory: Error - ${products.message}")
                            _state.value = _state.value.copy(
                                isLoading = false
                            )

                            _event.emit(
                                UiEvent.ToastMessage(
                                    message = products.message ?: "Unknown error"
                                )
                            )
                        }

                        is Resource.Loading -> {
                            Log.d("GetProductsViewModel", "getProductsByCategory: Loading")
                            _state.value = _state.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
                .collect()
        }
    }

    sealed class UiEvent {
        data class ToastMessage(val message: String) : UiEvent()
    }
}