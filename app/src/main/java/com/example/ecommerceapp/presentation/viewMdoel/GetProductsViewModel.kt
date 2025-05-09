package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.userCase.DeleteProductUseCase
import com.example.ecommerceapp.domain.userCase.GetProductsUseCase
import com.example.ecommerceapp.domain.userCase.InsertProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetProductsViewModel @Inject constructor(
    private val insertProductUseCase: InsertProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductsUseCase: GetProductsUseCase
): ViewModel(){

    private val _saveProducts = getProductsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val state get() = _saveProducts

    fun toggleProductSave(product: Product) {
        viewModelScope.launch {
            val isSaved = _saveProducts.value.any { it.id == product.id }
            if(isSaved) {
                deleteProductUseCase(product)
            } else {
                insertProductUseCase(product)
            }
        }
    }

    fun isProductSaved(product: Product): Boolean {
        return _saveProducts.value.any { it.id == product.id }
    }
}