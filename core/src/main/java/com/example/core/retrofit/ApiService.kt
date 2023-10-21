package com.example.core.retrofit

import com.example.core.retrofit.response.DetailProductsResponse
import com.example.core.retrofit.response.PagingProductsResponse
import com.example.core.retrofit.response.SearchProductsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getDataPagingProduct(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): PagingProductsResponse

    @GET("products/search")
    suspend fun getDataSearchProduct(
        @Query("q") query: String
    ): SearchProductsResponse

    @GET("products/{id}")
    suspend fun getDetailProductData(
        @Path("id") id: Int
    ): DetailProductsResponse

}