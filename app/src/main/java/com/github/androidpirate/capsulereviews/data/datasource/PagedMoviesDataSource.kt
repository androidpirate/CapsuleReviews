package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.PageKeyedDataSource
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesResponse
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType.ALL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagedMoviesDataSource(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val genericSort: GenericSortType,
    private val sort: SortType,
    private val genre: GenreType): PageKeyedDataSource<Int, NetworkMoviesListItem?>() {

    private var page = Constants.FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NetworkMoviesListItem?>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val response: NetworkMoviesResponse? = try {
                    if (genre == ALL) {
                        getPagedMoviesWithGenericSort(page, genericSort)
                    } else {
                        getPagedMoviesWithGenre(page, genre, sort)
                    }
                } catch (e: NoConnectivityException) {
                    null
                }

                if(response != null) {
                    callback.onResult(
                        response.networkMoviesListItems,
                        null,
                        page + Constants.PAGE_LOAD_INCREMENT)
                } else {
                    // If load failed, return a list of nulls of length Constants.PAGE_SIZE:
                    callback.onResult(
                        MutableList<NetworkMoviesListItem?>(Constants.PAGE_SIZE) { null },
                        null,
                        page + Constants.PAGE_LOAD_INCREMENT)
                }
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMoviesListItem?>) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val response: NetworkMoviesResponse? =
                        try {
                            if (genre == ALL) {
                                getPagedMoviesWithGenericSort(params.key, genericSort)
                            } else {
                                getPagedMoviesWithGenre(params.key, genre, sort)
                            }
                        } catch (e: NoConnectivityException) {
                            null
                        }

                    if(response != null) {
                        if (response.totalPages >= params.key) {
                            callback.onResult(
                                response.networkMoviesListItems, params.key + Constants.PAGE_LOAD_INCREMENT
                            )
                        }
                    } else {
                        // If load failed, return a list of nulls of length Constants.PAGE_SIZE:
                        callback.onResult(
                            MutableList<NetworkMoviesListItem?>(Constants.PAGE_SIZE) { null },
                            params.key + Constants.PAGE_LOAD_INCREMENT
                        )
                    }
                }
            }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMoviesListItem?>) {
        // Not used
    }

    private suspend fun getPagedMoviesWithGenericSort(
        page: Int,
        genericSort: GenericSortType): NetworkMoviesResponse {
            return when(genericSort) {
                POPULAR ->
                    api.getPagedPopularMovies(page)
                TOP_RATED ->
                    api.getPagedTopRatedMovies(page)
                NOW_PLAYING ->
                    api.getPagedNowPlayingMovies(page, BuildConfig.RELEASE_DATE_DESC)
                UPCOMING ->
                    api.getPagedUpcomingMovies(page, BuildConfig.RELEASE_DATE_DESC)
                TRENDING ->
                    api.getPagedTrendingMovies(page)
            }
    }

    private suspend fun getPagedMoviesWithGenre(
        page: Int,
        genre: GenreType,
        sort: SortType): NetworkMoviesResponse {
            return api.getPagedMoviesWithGenre(page, genre.id, sort.queryParameter)
    }
}