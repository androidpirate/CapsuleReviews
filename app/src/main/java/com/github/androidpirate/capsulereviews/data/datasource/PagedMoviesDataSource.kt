package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.PageKeyedDataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesResponse
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.SortType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagedMoviesDataSource(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val sort: SortType,
    private val genre: GenreType)
    : PageKeyedDataSource<Int, NetworkMoviesListItem>() {

    private lateinit var response: NetworkMoviesResponse
    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NetworkMoviesListItem>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                response = when (sort) {
                    POPULAR -> getPagedPopularMovies(page)
                    TOP_RATED -> getPagedTopRatedMovies(page)
                    NOW_PLAYING -> getPagedNowPlayingMovies(page)
                    UPCOMING -> getPagedUpcomingMovies(page)
                    TRENDING -> getPagedTrendingMovies(page)
                }
                callback.onResult(
                    response.networkMoviesListItems,
                    null,
                    page + INITIAL_LOAD_INCREMENT)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NetworkMoviesListItem>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                response = when(sort) {
                    POPULAR -> api.getPagedPopularMovies(params.key)
                    TOP_RATED -> api.getPagedTopRatedMovies(params.key)
                    NOW_PLAYING -> api.getPagedNowPlayingMovies(params.key)
                    UPCOMING -> api.getPagedUpcomingMovies(params.key)
                    TRENDING -> api.getPagedTrendingMovies(params.key)
                }
                if (response.totalPages >= params.key) {
                    callback.onResult(
                        response.networkMoviesListItems,
                        params.key + 1)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NetworkMoviesListItem>) {
    }

    private suspend fun getPagedPopularMovies(page: Int): NetworkMoviesResponse {
        return api.getPagedPopularMovies(page)
    }

    private suspend fun getPagedTopRatedMovies(page: Int): NetworkMoviesResponse {
        return api.getPagedTopRatedMovies(page)
    }

    private suspend fun getPagedNowPlayingMovies(page: Int): NetworkMoviesResponse {
        return api.getPagedNowPlayingMovies(page)
    }

    private suspend fun getPagedUpcomingMovies(page: Int): NetworkMoviesResponse {
        return api.getPagedUpcomingMovies(page)
    }

    private suspend fun getPagedTrendingMovies(page: Int): NetworkMoviesResponse {
        return api.getPagedTrendingMovies(page)
    }

    companion object {
        const val FIRST_PAGE = 1
        const val INITIAL_LOAD_INCREMENT = 1
    }
}