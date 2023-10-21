package com.example.core.room

import androidx.lifecycle.LiveData
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface FavoriteDao {

    @Query(
        "INSERT INTO favorite (id, " +
                "title," +
                "description, " +
                "price, " +
                "discountPercentage, " +
                "rating, " +
                "stock, " +
                "brand, " +
                "category, " +
                "thumbnail) values (:id, :title, :desc, :price, :discountPercentage, :rating, :stock, :brand, :category, :thumbnail)"
    )
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
    )

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteFavorite(id: Int)

    @Query("DELETE FROM favorite")
    fun deleteAllFavorite()

    @Query("SELECT * FROM favorite")
    fun getFavorite(): Flow<List<FavoriteEntity>?>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getFavoriteForDetail(id: Int): FavoriteEntity?
}