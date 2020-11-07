package com.github.androidpirate.capsulereviews.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.androidpirate.capsulereviews.data.db.entity.*

@Database(
    entities = [DBFavorite:: class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile
        private var instance: AppDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            return Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "capsule_reviews.db"
                    ).build()
        }
    }
}