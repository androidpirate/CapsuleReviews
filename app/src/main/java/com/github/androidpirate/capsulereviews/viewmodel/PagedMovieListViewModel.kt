package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedMovieListViewModel(private val repo: MoviesRepository) : ViewModel() {

    val popularMovies = repo.getPagedMovies(viewModelScope, POPULAR, ALL)
    val topRatedMovies = repo.getPagedMovies(viewModelScope, TOP_RATED, ALL)
    val nowPlayingMovies = repo.getPagedMovies(viewModelScope, NOW_PLAYING, ALL)
    val upcomingMovies = repo.getPagedMovies(viewModelScope, UPCOMING, ALL)
    val trendingMovies = repo.getPagedMovies(viewModelScope, TRENDING, ALL)
}