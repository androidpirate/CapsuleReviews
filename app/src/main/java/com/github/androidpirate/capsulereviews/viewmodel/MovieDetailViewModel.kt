package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.util.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailViewModel
    @ViewModelInject
    constructor(
    private val repo: MoviesRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val _movieDetails = MutableLiveData<NetworkMovie>()
    val movieDetails: LiveData<NetworkMovie>
    get() = _movieDetails

    private val _similarMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    val similarMovies: LiveData<List<NetworkMoviesListItem>>
    get() = _similarMovies

    private val _movieVideoKey = MutableLiveData<String>()
    val movieVideoKey: LiveData<String>
    get() = _movieVideoKey

    private var imdbEndpoint = MutableLiveData<String>()
    private var isFavorite = MutableLiveData<Boolean>()
    private var flagDecoration = false

    private val _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean>
    get() = _isOnline

    private fun setOffline() {
        _isOnline.postValue(false)
    }

    fun getMovieDetails(movieId: Int): LiveData<NetworkMovie> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val movieDetails = repo.fetchMovieDetails(movieId)
                    _movieDetails.postValue(movieDetails)
                    setIsFavorite(movieDetails.id)
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return movieDetails
    }

    fun getSimilarMovies(movieId: Int): LiveData<List<NetworkMoviesListItem>> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _similarMovies.postValue(repo.fetchSimilarMovies(movieId))
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return similarMovies
    }

    fun getMovieKey(movieId: Int): LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val movieVideos = repo.fetchMovieVideos(movieId)
                    _movieVideoKey.postValue(repo.getMovieVideoKey(movieVideos))
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return movieVideoKey
    }

    fun getIsFavorite(): LiveData<Boolean> {
        return isFavorite
    }

    private fun setIsFavorite(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isFavorite.postValue(favRepo.isFavorite(itemId))
            }
        }
    }

    fun insertFavoriteMovie(movie: NetworkMovie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favRepo.insertFavorite(movie.toFavorite())
                isFavorite.postValue(true)
            }
        }
    }

    fun deleteFavoriteMovie(movie: NetworkMovie) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favRepo.deleteFavorite(movie.toFavorite())
                isFavorite.postValue(false)
            }
        }
    }

    fun setImdbEndpoint(imdbEndpoint: String) {
        this.imdbEndpoint.apply { value = imdbEndpoint }
    }

    fun getImdbEndpoint(): LiveData<String> {
        return imdbEndpoint
    }

    fun setFlagDecorationOn() {
        flagDecoration = true
    }

    fun setFlagDecorationOff() {
        flagDecoration = false
    }

    fun getFlagDecoration(): Boolean {
        return flagDecoration
    }
}