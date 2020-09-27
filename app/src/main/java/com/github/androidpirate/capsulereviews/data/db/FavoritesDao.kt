package com.github.androidpirate.capsulereviews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.util.internal.Constants

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    fun getFavorites(): LiveData<List<DBFavorite>>

    @Query("SELECT * FROM favorites WHERE type = :typeMovie")
    fun getFavoriteMovies(typeMovie: String = Constants.FAVORITE_MOVIE_TYPE): LiveData<List<DBFavorite>>

    @Query("SELECT * FROM favorites WHERE type = :typeTvShow")
    fun getFavoriteTvShows(typeTvShow: String = Constants.FAVORITE_TV_SHOW_TYPE): LiveData<List<DBFavorite>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<DBFavorite>

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :id)")
    fun isRowIsExist(id : Int) : Boolean

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteWithId(id: Int)

    @Query("UPDATE favorites SET bingeStatus = :status WHERE id = :id")
    suspend fun updateFavoriteStatus(status: String, id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: DBFavorite)

    @Delete
    suspend fun deleteFavorite(favorite: DBFavorite)

}