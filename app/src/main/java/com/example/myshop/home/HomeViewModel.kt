package com.example.myshop.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.Repository
import com.example.core.SealedClass
import com.example.core.retrofit.response.DetailProductsResponse
import com.example.core.retrofit.response.Products
import com.example.core.retrofit.response.SearchProductsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val getPagingProduct: () -> Flow<PagingData<Products>> = {
        repository.getProductPaging()
            .cachedIn(viewModelScope)
    }

    private val _productSearchData = MutableStateFlow<SealedClass<SearchProductsResponse>>(SealedClass.Init)
    val productSearchData = _productSearchData

    fun getSearchProductData(query: String) = viewModelScope.launch {

        _productSearchData.emit(SealedClass.Loading)

        repository.getSearchProductData(query).catch {
            _productSearchData.emit(SealedClass.Error(it))
        }.collect {
            _productSearchData.emit(SealedClass.Success(it))
        }
    }

}