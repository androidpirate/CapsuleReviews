package com.github.androidpirate.capsulereviews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.androidpirate.capsulereviews.data.db.entity.*

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPopularMovies(popularMovies: List<DBPopularMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTopRatedMovies(topRatedMovies: List<DBTopRatedMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNowPlayingMovies(nowPlayingMovies: List<DBNowPlayingMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUpcomingMovies(upcomingMovies: List<DBUpcomingMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTrendingMovies(trendingMovies: List<DBTrendingMovie>)

    @Query("SELECT * FROM popular_movies")
    suspend fun getPopularMovies(): LiveData<List<DBPopularMovie>>

    @Query("SELECT * FROM top_rated_movies")
    suspend fun getTopRatedMovies(): LiveData<List<DBPopularMovie>>

    @Query("SELECT * FROM now_playing_movies")
    suspend fun getNowPlayingMovies(): LiveData<List<DBPopularMovie>>

    @Query("SELECT * FROM upcoming_movies")
    suspend fun getUpComingMovies(): LiveData<List<DBPopularMovie>>

    @Query("SELECT * FROM trending_movies")
    suspend fun getTrendingMovies(): LiveData<List<DBPopularMovie>>
}