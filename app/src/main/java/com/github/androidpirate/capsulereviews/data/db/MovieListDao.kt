package com.github.androidpirate.capsulereviews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.androidpirate.capsulereviews.data.db.entity.*

@Dao
interface MovieListDao {

    @Query("UPDATE movies SET popular = 1 WHERE id = :movieId")
    suspend fun updatePopularMovie(movieId: Int)

    @Insert
    suspend fun insertPopularMovie(movie: DBMovie)

    @Query("UPDATE movies SET topRated = 1 WHERE id = :movieId")
    suspend fun updateTopRatedMovie(movieId: Int)

    @Insert
    suspend fun insertTopRatedMovie(movie: DBMovie)

    @Query("UPDATE movies SET nowPlaying = 1 WHERE id = :movieId")
    suspend fun updateNowPlayingMovie(movieId: Int)

    @Insert
    suspend fun insertNowPlayingMovie(movie: DBMovie)

    @Query("UPDATE movies SET upcoming = 1 WHERE id = :movieId")
    suspend fun updateUpcomingMovie(movieId: Int)

    @Insert
    suspend fun insertUpcomingMovie(movie: DBMovie)

    @Query("UPDATE movies SET trending = 1 WHERE id = :movieId")
    suspend fun updateTrendingMovie(movieId: Int)

    @Insert
    suspend fun insertTrendingMovie(movie: DBMovie)

    @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :id)")
    fun isRowIsExist(id : Int) : Boolean

    @Query("SELECT * FROM movies WHERE popular = 1")
    fun getPopularMovies(): LiveData<List<DBMovie>>

    @Query("SELECT * FROM movies WHERE topRated = 1")
    fun getTopRatedMovies(): LiveData<List<DBMovie>>

    @Query("SELECT * FROM movies WHERE nowPlaying = 1")
    fun getNowPlayingMovies(): LiveData<List<DBMovie>>

    @Query("SELECT * FROM movies WHERE upcoming = 1")
    fun getUpComingMovies(): LiveData<List<DBMovie>>

    @Query("SELECT * FROM movies WHERE trending = 1")
    fun getTrendingMovies(): LiveData<List<DBMovie>>

}