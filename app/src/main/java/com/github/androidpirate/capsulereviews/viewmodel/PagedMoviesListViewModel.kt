package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedMoviesListViewModel(repo: MoviesRepository) : ViewModel() {

    val popularMovies = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        genre = ALL)

    val topRatedMovies = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = TOP_RATED,
        sort = POPULAR_DESCENDING,
        genre = ALL)

    val nowPlayingMovies = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = NOW_PLAYING,
        sort = POPULAR_DESCENDING,
        genre = ALL)

    val upcomingMovies = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = UPCOMING,
        sort = POPULAR_DESCENDING,
        genre = ALL)

    val trendingMovies = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = TRENDING,
        sort = POPULAR_DESCENDING,
        genre = ALL)
}