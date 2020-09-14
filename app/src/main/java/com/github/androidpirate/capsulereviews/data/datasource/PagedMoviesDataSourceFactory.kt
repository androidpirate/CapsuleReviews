package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.DataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import kotlinx.coroutines.CoroutineScope

class PagedMoviesDataSourceFactory(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val genericSort: GenericSortType,
    private val sort: SortType,
    private val genre: GenreType)
    : DataSource.Factory<Int, NetworkMoviesListItem>() {

    override fun create(): DataSource<Int, NetworkMoviesListItem> {
        return PagedMoviesDataSource(api, scope, genericSort, sort, genre)
    }
}