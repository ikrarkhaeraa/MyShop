package com.example.core.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.retrofit.ApiService
import com.example.core.retrofit.response.Products

class PagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, Products>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Products> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getDataPagingProduct(
                params.loadSize,
                page
            )

            LoadResult.Page(
                data = responseData.products,
                prevKey = null,
                nextKey = if (page == responseData.total) null else page.plus(1)
            )
        } catch (exception: Exception) {
            Log.d("pagingError", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Products>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}