package com.github.androidpirate.capsulereviews.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow

@Dao
interface TvShowListDao {

    @Query("UPDATE tv_shows SET popular = 1 WHERE id = :showId")
    suspend fun updatePopularTvShow(showId: Int)

    @Insert
    suspend fun insertPopularTvShow(show: DBTvShow)

    @Query("UPDATE tv_shows SET topRated = 1 WHERE id = :showId")
    suspend fun updateTopRatedTvShow(showId: Int)

    @Insert
    suspend fun insertTopRatedTvShow(show: DBTvShow)

    @Query("UPDATE tv_shows SET trending = 1 WHERE id = :showId")
    suspend fun updateTrendingTvShow(showId: Int)

    @Insert
    suspend fun insertTrendingTvShow(show: DBTvShow)

    @Query("SELECT EXISTS(SELECT * FROM tv_shows WHERE id = :id)")
    suspend fun isRowExist(id: Int) : Boolean

    @Query("SELECT * FROM tv_shows WHERE popular = 1")
    fun getPopularTvShows(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tv_shows WHERE topRated = 1")
    fun getTopRatedTvShows(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tv_shows WHERE trending = 1")
    fun getTrendingTvShows(): LiveData<List<DBTvShow>>
}