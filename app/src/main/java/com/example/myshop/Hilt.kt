package com.example.myshop

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core.Repository
import com.example.core.retrofit.ApiService
import com.example.core.room.FavoriteDao
import com.example.core.room.RoomDatabase
import com.example.myshop.detail.DetailViewModel
import com.example.myshop.favorite.FavoriteViewModel
import com.example.myshop.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Hilt {

    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideApiService(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }


    @Provides
    fun provideRespository(
        apiService: ApiService,
        favoriteDao: FavoriteDao
    ): Repository {
        return Repository(apiService, favoriteDao)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RoomDatabase::class.java,
            "productAdded.db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providefavoriteDao(database: RoomDatabase): FavoriteDao {
        return database.favoriteDao()
    }

}