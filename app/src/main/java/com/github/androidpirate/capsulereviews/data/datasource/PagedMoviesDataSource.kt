package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.PageKeyedDataSource
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesResponse
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagedMoviesDataSource(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val genericSort: GenericSortType,
    private val sort: SortType,
    private val genre: GenreType): PageKeyedDataSource<Int, NetworkMoviesListItem>() {

    private lateinit var response: NetworkMoviesResponse
    private var page = Constants.FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NetworkMoviesListItem>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                response =
                    if(genre == ALL) {
                        getPagedMoviesWithGenericSort(page, genericSort)
                    } else {
                        getPagedMoviesWithGenre(page, genre, sort)
                    }
                callback.onResult(
                    response.networkMoviesListItems,
                    null,
                    page + Constants.PAGE_LOAD_INCREMENT)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMoviesListItem>) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    response =
                        if(genre == ALL) {
                            getPagedMoviesWithGenericSort(params.key, genericSort)
                        } else {
                            getPagedMoviesWithGenre(params.key, genre, sort)
                        }

                    if(response.totalPages >= params.key) {
                        callback.onResult(
                            response.networkMoviesListItems,
                            params.key + Constants.PAGE_LOAD_INCREMENT)
                    }
                }
            }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMoviesListItem>) {
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