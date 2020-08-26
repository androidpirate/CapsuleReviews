package com.github.androidpirate.capsulereviews.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailViewModel(private val repo: MoviesRepository): ViewModel() {

    private val movieDetails = MutableLiveData<NetworkMovie>()
    private val movieVideos = MutableLiveData<List<NetworkVideosListItem>>()
    private val similarMovies = MutableLiveData<List<NetworkMoviesListItem>>()

    fun getMovieDetails(movieId: Int): LiveData<NetworkMovie> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieDetails.postValue(repo.fetchMovieDetails(movieId))
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

    fun getMovieVideos(movieId: Int): LiveData<List<NetworkVideosListItem>> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieVideos.postValue(repo.fetchMovieVideos(movieId))
            }
        }
        return movieVideos
    }
}