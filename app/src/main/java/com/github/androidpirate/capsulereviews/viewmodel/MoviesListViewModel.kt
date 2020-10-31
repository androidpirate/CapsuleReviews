package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.util.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesListViewModel
    @ViewModelInject
    constructor(
    private val repo: MoviesRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val _popularMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val popularMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val topRatedMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _topRatedMovies

    private val _nowPlayingMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val nowPlayingMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _nowPlayingMovies

    private val _upComingMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val upcomingMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _upComingMovies

    private val _trendingMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val trendingMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _trendingMovies

    val showcaseMovie = repo.showcaseMovie
    val showcaseVideoKey = repo.showcaseVideoKey
    private var isShowcaseFavorite = MutableLiveData<Boolean>(false)

    private val _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean>
    get() = _isOnline

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _popularMovies.postValue(repo.fetchPopularMovies())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _topRatedMovies.postValue(repo.fetchTopRatedMovies())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _nowPlayingMovies.postValue(repo.fetchNowPlayingMovies())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _upComingMovies.postValue(repo.fetchUpcomingMovies())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _trendingMovies.postValue(repo.fetchTrendingMovies())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
    }

    private fun setOffline() {
        _isOnline.postValue(false)
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

    fun insertShowcaseMovieToFavorites(showcaseMovie: NetworkMovie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = showcaseMovie.toFavorite()
                favRepo.insertFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(true)
            }
        }
    }

    fun deleteShowcaseMovieFromFavorites(showcaseMovie: NetworkMovie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = showcaseMovie.toFavorite()
                favRepo.deleteFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(false)
            }
        }
    }
}