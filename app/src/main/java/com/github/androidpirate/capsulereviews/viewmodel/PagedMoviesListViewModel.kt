package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedMoviesListViewModel
    @ViewModelInject
    constructor(
    private val repo: MoviesRepository) : ViewModel() {

    val genericSort = MutableLiveData<GenericSortType>(POPULAR)

    fun setGenericSort(genericSortType: GenericSortType) = apply { genericSort.value = genericSortType }

    val moviesByGenericSortType: LiveData<PagedList<NetworkMoviesListItem>> =
        Transformations.switchMap(genericSort, ::getMoviesByGenericSort)

    // TODO 2: The problem here is I am using the function below with Transformations.switchMap
    // TODO 2: Even though I could able to throw the exception from PagedMoviesDataSource
    // TODO 2: no idea how to catch it here.
    // TODO 2: Things worked pretty cool in MoviesListViewModel and MovieDetailViewModel.
    private fun getMoviesByGenericSort(genericSort: GenericSortType) = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = genericSort,
        sort = POPULAR_DESCENDING,
        genre = ALL
    )

    val genre = MutableLiveData<GenreType>(ALL)

    fun setGenre(movieGenre: GenreType) = apply { genre.value = movieGenre }

    val moviesByGenre: LiveData<PagedList<NetworkMoviesListItem>> =
        Transformations.switchMap(genre, ::getMoviesByGenre)

    private fun getMoviesByGenre(genre: GenreType) = repo.getPagedMovies(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        genre = genre
    )

    private var flagDecoration = false

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