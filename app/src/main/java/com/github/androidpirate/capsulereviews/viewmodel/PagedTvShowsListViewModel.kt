package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedTvShowsListViewModel
    @ViewModelInject
    constructor(
    private val repo: TvShowsRepository): ViewModel() {

    val genericSort = MutableLiveData<GenericSortType>(POPULAR)

    fun setGenericSort(genericSortType: GenericSortType) = apply { genericSort.value = genericSortType }

    val tvShowsByGenericSortType: LiveData<PagedList<NetworkTvShowsListItem?>> =
        Transformations.switchMap(genericSort, ::getTvShowsByGenericSort)

    private fun getTvShowsByGenericSort(genericSort: GenericSortType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = genericSort,
        sort = POPULAR_DESCENDING,
        network = NetworkType.ALL,
        genre = GenreType.ALL
    )

    val genre = MutableLiveData<GenreType>(GenreType.ALL)

    fun getGenre(): GenreType {
        return genre.value!!
    }

    fun setGenre(tvGenre: GenreType) = apply { genre.value = tvGenre }

    val tvShowsByGenre: LiveData<PagedList<NetworkTvShowsListItem?>> =
        Transformations.switchMap(genre, ::getTvShowsByGenre)

    private fun getTvShowsByGenre(genre: GenreType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = network.value!!,
        genre = genre
    )

    val network = MutableLiveData<NetworkType>(NetworkType.ALL)

    fun getNetwork(): NetworkType {
        return network.value!!
    }

    fun setNetwork(tvNetwork: NetworkType) = apply { network.value = tvNetwork }

    val tvShowsByNetwork: LiveData<PagedList<NetworkTvShowsListItem?>> =
        Transformations.switchMap(network, ::getTvShowsByNetwork)

    private fun getTvShowsByNetwork(network: NetworkType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = network,
        genre = genre.value!!
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

    fun refreshDataByGenericSort(genericSort: GenericSortType) {
        setGenericSort(genericSort)
    }

    fun refreshDataByNetwork(network: NetworkType) {
        setNetwork(network)
    }

    fun refreshDataByGenre(tvGenre: GenreType) {
        setGenre(tvGenre)
    }
}