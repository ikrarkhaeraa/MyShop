package com.example.core.retrofit.response

import com.google.gson.annotations.SerializedName

data class PagingProductsResponse (

    @field:SerializedName("products")
    val products: List<Products>,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("skip")
    val skip: Int,

    @field:SerializedName("limit")
    val limit: Int
)

data class Products (
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("discountPercentage")
    val discountPercentage: Float,

    @field:SerializedName("rating")
    val rating: Float,

    @field:SerializedName("stock")
    val stock: Int,

    @field:SerializedName("brand")
    val brand: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("thumbnail")
    val thumbnail: String,

    @field:SerializedName("images")
    val images: List<String>
)
