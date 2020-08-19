package com.github.androidpirate.capsulereviews.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.androidpirate.capsulereviews.data.db.entity.*

@Database(entities = [
    DBPopularMovie::class,
    DBTopRatedMovie::class,
    DBNowPlayingMovie::class,
    DBUpcomingMovie::class,
    DBTrendingMovie::class], version = 1, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun movieListDao(): MovieListDao

    companion object {
        @Volatile
        private var instance: MoviesDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) : MoviesDatabase {
            return Room.databaseBuilder(
                        context,
                        MoviesDatabase::class.java,
                        "movies.db"
                    ).build()
        }
    }
}