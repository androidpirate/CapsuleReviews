package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.DataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import kotlinx.coroutines.CoroutineScope

class PagedTvShowsDataSourceFactory(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val genericSort: GenericSortType,
    private val sort: SortType,
    private val network: NetworkType,
    private val genre: GenreType): DataSource.Factory<Int, NetworkTvShowsListItem>() {

    override fun create(): DataSource<Int, NetworkTvShowsListItem?> {
        return PagedTvShowsDataSource(api, scope, genericSort, sort, network, genre)
    }
}