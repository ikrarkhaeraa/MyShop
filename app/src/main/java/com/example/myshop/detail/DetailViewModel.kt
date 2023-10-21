package com.example.myshop.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Repository
import com.example.core.SealedClass
import com.example.core.retrofit.response.DetailProductsResponse
import com.example.core.room.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val _productDetailData = MutableStateFlow<SealedClass<DetailProductsResponse>>(SealedClass.Init)
    val productDetailData = _productDetailData

    fun getDetailProductData(id: Int) = viewModelScope.launch {

        _productDetailData.emit(SealedClass.Loading)

        repository.getDetailProductData(id).catch {
            _productDetailData.emit(SealedClass.Error(it))
        }.collect {
            _productDetailData.emit(SealedClass.Success(it))
        }
    }

    fun addFavorite(
        id: Int,
        title: String,
        desc: String,
        price: Int,
        discountPercentage: Float,
        rating: Float,
        stock: Int,
        brand: String,
        category: String,
        thumbnail: String
    ) {
        return repository.addFavorite(
            id,
            title,
            desc,
            price,
            discountPercentage,
            rating,
            stock,
            brand,
            category,
            thumbnail
        )
    }

    fun deleteFavorite(id: Int) {
        return repository.deleteFavorite(id)
    }

    suspend fun getFavoriteForDetail(id: Int): FavoriteEntity? {
        return repository.getFavoriteForDetail(id)
    }

}