package com.example.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var instance: com.example.core.room.RoomDatabase? = null
        fun getInstance(context: Context): com.example.core.room.RoomDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    com.example.core.room.RoomDatabase::class.java,
                    "productAdded.db"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
    }
}