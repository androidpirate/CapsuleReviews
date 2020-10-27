package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailViewModel
    @ViewModelInject
    constructor(
    private val repo: MoviesRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val movieDetails = MutableLiveData<NetworkMovie>()
    private val movieVideoKey = MutableLiveData<String>()
    private val similarMovies = MutableLiveData<List<NetworkMoviesListItem>>()
    private var imdbEndpoint = MutableLiveData<String>()
    private var isFavorite = MutableLiveData<Boolean>()
    private var flagDecoration = false

    fun getMovieDetails(movieId: Int): LiveData<NetworkMovie> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val networkMovie = repo.fetchMovieDetails(movieId)
                movieDetails.postValue(networkMovie)
                setIsFavorite(networkMovie.id)
            }
        }
        return movieDetails
    }

    fun getSimilarMovies(movieId: Int): LiveData<List<NetworkMoviesListItem>> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                similarMovies.postValue(repo.fetchSimilarMovies(movieId))
            }
        }
        return similarMovies
    }

    fun getMovieKey(movieId: Int): LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieVideoKey.postValue(repo.fetchVideoKey(movieId))
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