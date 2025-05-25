package com.example.ecommerceapp.presentation.viewMdoel.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.core.Resource
import com.example.ecommerceapp.domain.userCase.GetSomeProductsUseCase
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
class SomeProductsViewModel @Inject constructor(
    private val getSomeProductsUseCase: GetSomeProductsUseCase,
) : ViewModel() {

    private var _getSomeProducts: MutableStateFlow<GetProductsState> =
        MutableStateFlow((GetProductsState()))
    val getSomeProducts = _getSomeProducts.asStateFlow()

    private var _event: MutableSharedFlow<UiEvent> = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchSomeProducts()
    }

    fun fetchSomeProducts() {
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
    sealed class UiEvent {
        data class ToastMessage(val message: String) : UiEvent()
    }
}