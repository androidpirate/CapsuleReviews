package com.github.androidpirate.capsulereviews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.androidpirate.capsulereviews.data.db.entity.*

@Dao
interface TvShowListDao {

    @Query("UPDATE tvShows SET popular = 1 WHERE id = :showId")
    suspend fun updatePopularTvShow(showId: Int)

    @Insert
    suspend fun insertPopularTvShow(show: DBTvShow)

    @Query("UPDATE tvShows SET topRated = 1 WHERE id = :showId")
    suspend fun updateTopRatedTvShow(showId: Int)

    @Insert
    suspend fun insertTopRatedTvShow(show: DBTvShow)

    @Query("UPDATE tvShows SET trending = 1 WHERE id = :showId")
    suspend fun updateTrendingTvShow(showId: Int)

    @Insert
    suspend fun insertTrendingTvShow(show: DBTvShow)

    @Query("UPDATE tvShows SET netflix = 1 WHERE id = :showId")
    suspend fun updatePopularTvShowOnNetflix(showId: Int)

    @Insert
    suspend fun insertPopularTvShowOnNetflix(show: DBTvShow)

    @Query("UPDATE tvShows SET hulu = 1 WHERE id = :showId")
    suspend fun updatePopularTvShowOnHulu(showId: Int)

    @Insert
    suspend fun insertPopularTvShowOnHulu(show: DBTvShow)

    @Query("UPDATE tvShows SET disneyPlus = 1 WHERE id = :showId")
    suspend fun updatePopularTvShowOnDisneyPlus(showId: Int)

    @Insert
    suspend fun insertPopularTvShowOnDisneyPlus(show: DBTvShow)

    @Query("SELECT EXISTS(SELECT * FROM tvShows WHERE id = :id)")
    suspend fun isRowExist(id: Int) : Boolean

    @Query("SELECT * FROM tvShows WHERE popular = 1")
    fun getPopularTvShows(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tvShows WHERE topRated = 1")
    fun getTopRatedTvShows(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tvShows WHERE trending = 1")
    fun getTrendingTvShows(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tvShows WHERE netflix = 1")
    fun getPopularShowsOnNetflix(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tvShows WHERE hulu = 1")
    fun getPopularShowsOnHulu(): LiveData<List<DBTvShow>>

    @Query("SELECT * FROM tvShows WHERE disneyPlus = 1")
    fun getPopularShowsOnDisneyPlus(): LiveData<List<DBTvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShowcaseTvShow(showcaseTvShow: DbTvShowShowcase)

    @Query("SELECT * FROM tvShowcase")
    fun getShowcaseTvShow(): LiveData<DbTvShowShowcase>
}