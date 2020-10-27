package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesListViewModel
    @ViewModelInject
    constructor(
    private val repo: MoviesRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    val popularMovies = repo.getPopularMovies()
    val topRatedMovies = repo.getTopRatedMovies()
    val nowPlayingMovies = repo.getNowPlayingMovies()
    val upcomingMovies = repo.getUpcomingMovies()
    val trendingMovies = repo.getTrendingMovies()
    val showcaseMovie = repo.getShowcaseMovie()
    private var isShowcaseFavorite = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistPopularMovies()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistTopRatedMovies()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistNowPlayingMovies()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistUpcomingMovies()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistTrendingMovies()
            }
        }
    }

    fun getIsShowcaseFavorite(): LiveData<Boolean> {
        return isShowcaseFavorite
    }

    fun setIsShowcaseFavorite(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isShowcaseFavorite.postValue(favRepo.isFavorite(itemId))
            }
        }
    }

    fun insertShowcaseMovieToFavorites(showcaseMovie: DBMovieShowcase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = repo.fetchMovieDetails(showcaseMovie.movieId).toFavorite()
                favRepo.insertFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(true)
            }
        }
    }

    fun deleteShowcaseMovieFromFavorites(showcaseMovie: DBMovieShowcase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = repo.fetchMovieDetails(showcaseMovie.movieId).toFavorite()
                favRepo.deleteFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(false)
            }
        }
    }
}