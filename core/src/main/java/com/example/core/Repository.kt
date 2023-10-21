package com.example.core

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.paging.PagingSource
import com.example.core.retrofit.ApiService
import com.example.core.retrofit.response.Products
import com.example.core.room.FavoriteDao
import com.example.core.room.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) {

    fun getProductPaging(): Flow<PagingData<Products>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                PagingSource(apiService)
            }
        ).flow
    }

    fun getDetailProductData(id: Int) = flow {
        emit(apiService.getDetailProductData(id))
    }

    fun getSearchProductData(query: String) = flow {
        emit(apiService.getDataSearchProduct(query))
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
        return favoriteDao.addFavorite(
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
        return favoriteDao.deleteFavorite(id)
    }

    fun deleteAllFavorite() {
        return favoriteDao.deleteAllFavorite()
    }

    fun getFavorite(): Flow<List<FavoriteEntity>?> {
        return favoriteDao.getFavorite()
    }

    suspend fun getFavoriteForDetail(id: Int): FavoriteEntity? {
        return favoriteDao.getFavoriteForDetail(id)
    }

}