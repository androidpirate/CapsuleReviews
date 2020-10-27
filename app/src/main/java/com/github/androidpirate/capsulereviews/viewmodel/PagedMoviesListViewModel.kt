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