package com.example.core.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite")
class FavoriteEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo("title")
    val title: String,

    @field:ColumnInfo("description")
    val description: String,

    @field:ColumnInfo("price")
    val price: Int,

    @field:ColumnInfo("discountPercentage")
    val discountPercentage: Float,

    @field:SerializedName("rating")
    val rating: Float,

    @field:ColumnInfo("stock")
    val stock: Int,

    @field:ColumnInfo("brand")
    val brand: String,

    @field:ColumnInfo("category")
    var category: String,

    @field:ColumnInfo("thumbnail")
    var thumbnail: String,

    )