package com.example.ecommerceapp.presentation.viewMdoel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerceapp.domain.model.Product
import com.example.ecommerceapp.domain.userCase.SearchingProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingProductsViewModel @Inject constructor(
    private val searchingProductsUseCase: SearchingProductsUseCase
): ViewModel() {

    private val _searchResult = MutableStateFlow<PagingData<Product>>(PagingData.empty())
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