package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListViewModel(private val repo: MoviesRepository): ViewModel() {

    val popularMovies = repo.getPopularMovies()
    val topRatedMovies = repo.getTopRatedMovies()
    val nowPlayingMovies = repo.getNowPlayingMovies()
    val upcomingMovies = repo.getUpcomingMovies()
    val trendingMovies = repo.getTrendingMovies()
    val showcaseMovie = repo.getShowcaseMovie()

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
}