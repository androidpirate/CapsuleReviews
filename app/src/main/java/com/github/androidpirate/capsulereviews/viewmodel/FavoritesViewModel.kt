package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel
    @ViewModelInject
    constructor(
    private val repo: FavoritesRepository): ViewModel() {

    val favoriteMovies = repo.getFavoriteMovies()
    val favoriteTvShows = repo.getFavoriteTvShows()

    fun getFavoriteMovie(movieId: Int): LiveData<DBFavorite> {
        return repo.getFavoriteMovie(movieId)
    }

    fun getFavoriteTvShow(showId: Int): LiveData<DBFavorite> {
        return repo.getFavoriteTvShow(showId)
    }

    fun updateFavoriteStatus(status: String, itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.updateFavoriteStatus(status, itemId)
            }
        }
    }

    fun deleteFavoriteWithId(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.deleteFavoriteWithId(itemId)
            }
        }
    }
}