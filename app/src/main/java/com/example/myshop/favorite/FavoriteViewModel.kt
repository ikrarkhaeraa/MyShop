package com.example.myshop.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.core.Repository
import com.example.core.room.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun deleteFavorite(id: Int) {
        return repository.deleteFavorite(id)
    }

    fun deleteAllFavorite() {
        return repository.deleteAllFavorite()
    }

    fun getFavorite(): Flow<List<FavoriteEntity>?> {
        return repository.getFavorite()
    }

}