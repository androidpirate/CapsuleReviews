package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedTvShowsListViewModel(private val repo: TvShowsRepository): ViewModel() {

    val genericSort = MutableLiveData<GenericSortType>(POPULAR)

    fun setGenericSort(genericSortType: GenericSortType) = apply { genericSort.value = genericSortType }

    val tvShowsByGenericSortType: LiveData<PagedList<NetworkTvShowsListItem>> =
        Transformations.switchMap(genericSort, ::getTvShowsByGenericSort)

    private fun getTvShowsByGenericSort(genericSort: GenericSortType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = genericSort,
        sort = POPULAR_DESCENDING,
        network = NetworkType.ALL,
        genre = ALL
    )

    val genre = MutableLiveData<GenreType>(ALL)

    fun setGenre(tvGenre: GenreType) = apply { genre.value = tvGenre }

    val tvShowsByGenre: LiveData<PagedList<NetworkTvShowsListItem>> =
        Transformations.switchMap(genre, ::getTvShowsByGenre)

    private fun getTvShowsByGenre(genre: GenreType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = NetworkType.ALL,
        genre = genre
    )

    val network = MutableLiveData<NetworkType>(NetworkType.ALL)

    fun setNetwork(tvNetwork: NetworkType) = apply { network.value = tvNetwork }

    val tvShowsByNetwork: LiveData<PagedList<NetworkTvShowsListItem>> =
        Transformations.switchMap(network, ::getTvShowsByNetwork)

    private fun getTvShowsByNetwork(network: NetworkType) = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = network,
        genre = ALL
    )
}