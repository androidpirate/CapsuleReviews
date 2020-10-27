package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import com.github.androidpirate.capsulereviews.data.db.FavoritesDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import javax.inject.Inject

class FavoritesRepository
    @Inject
    constructor(private val dao: FavoritesDao) {

    fun getFavoriteMovies(): LiveData<List<DBFavorite>> {
        return dao.getFavoriteMovies()
    }

    fun getFavoriteMovie(movieId: Int): LiveData<DBFavorite> {
        return dao.getFavoriteById(movieId)
    }

    fun getFavoriteTvShow(tvShowId: Int): LiveData<DBFavorite> {
        return dao.getFavoriteById(tvShowId)
    }

    fun getFavoriteTvShows(): LiveData<List<DBFavorite>> {
        return dao.getFavoriteTvShows()
    }

    fun isFavorite(itemId: Int): Boolean {
        return dao.isRowIsExist(itemId)
    }

    suspend fun insertFavorite(favorite: DBFavorite) {
        dao.insertFavorite(favorite)
    }

    suspend fun updateFavoriteStatus(status: String, id: Int) {
        dao.updateFavoriteStatus(status, id)
    }

    suspend fun deleteFavorite(favorite: DBFavorite) {
        dao.deleteFavorite(favorite)
    }

    suspend fun deleteFavoriteWithId(id: Int) {
        dao.deleteFavoriteWithId(id)
    }
}