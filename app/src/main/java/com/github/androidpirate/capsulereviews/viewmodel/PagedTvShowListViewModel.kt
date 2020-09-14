package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import com.github.androidpirate.capsulereviews.util.internal.NetworkType.*
import com.github.androidpirate.capsulereviews.util.internal.SortType.*

class PagedTvShowListViewModel(repo: TvShowsRepository): ViewModel() {

    val popularTvShows = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = NONE,
        genre = ALL
    )

    val topRatedTvShows = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = TOP_RATED,
        sort = POPULAR_DESCENDING,
        network = NONE,
        genre = ALL
    )

    val trendingTvShows = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = TRENDING,
        sort = POPULAR_DESCENDING,
        network = NONE,
        genre = ALL
    )

    val trendingTvShowsOnNetflix = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = NETFLIX,
        genre = ALL
    )

    val trendingTvShowsOnHulu = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = HULU,
        genre = ALL
    )

    val trendingTvShowsOnDisney = repo.getPagedTvShows(
        scope = viewModelScope,
        genericSort = POPULAR,
        sort = POPULAR_DESCENDING,
        network = DISNEY_PLUS,
        genre = ALL
    )
}