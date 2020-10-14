package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.PageKeyedDataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsResponse
import com.github.androidpirate.capsulereviews.util.internal.SortType
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagedTvShowsDataSource (
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val genericSort: GenericSortType,
    private val sort: SortType,
    private val network: NetworkType,
    private val genre: GenreType
): PageKeyedDataSource<Int, NetworkTvShowsListItem> (){

    private lateinit var response: NetworkTvShowsResponse
    private var page = Constants.FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NetworkTvShowsListItem>
    ) {
        scope.launch {
            withContext(Dispatchers.IO) {
                response =
                    if(genre == GenreType.ALL && network == NetworkType.ALL) {
                        getPagedTvShowsWithGenericSort(page, genericSort)
                    } else if(genre == GenreType.ALL && network != NetworkType.ALL) {
                        getPagedTvShowsWithNetwork(page, network, sort)
                    } else if(genre != GenreType.ALL && network == NetworkType.ALL) {
                        getPagedTvShowsWithGenre(page, genre, sort)
                    } else {
                        getPagedTvShowsWithGenreAndNetwork(page, genre, network, sort)
                    }
                callback.onResult(
                    response.networkTvShowsListItems,
                    null,
                    page + Constants.PAGE_LOAD_INCREMENT
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NetworkTvShowsListItem>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                response =
                    if(genre == GenreType.ALL && network == NetworkType.ALL) {
                        getPagedTvShowsWithGenericSort(params.key, genericSort)
                    } else if(genre == GenreType.ALL && network != NetworkType.ALL) {
                        getPagedTvShowsWithNetwork(params.key, network, sort)
                    } else if(genre != GenreType.ALL && network == NetworkType.ALL) {
                        getPagedTvShowsWithGenre(params.key, genre, sort)
                    } else {
                        getPagedTvShowsWithGenreAndNetwork(params.key, genre, network, sort)
                    }
                if(response.totalPages >= params.key) {
                    callback.onResult(
                        response.networkTvShowsListItems,
                        params.key + Constants.PAGE_LOAD_INCREMENT
                    )
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NetworkTvShowsListItem>) {
        TODO("Not yet implemented")
    }

    private suspend fun getPagedTvShowsWithGenericSort(
        page: Int,
        genericSort: GenericSortType)
            : NetworkTvShowsResponse {
            return when(genericSort) {
                POPULAR, UPCOMING, NOW_PLAYING -> api.getPagedPopularTvShows(page)
                TOP_RATED -> api.getPagedTopRatedTvShows(page)
                TRENDING -> api.getPagedTrendingTvShows(page)
        }
    }

    private suspend fun getPagedTvShowsWithNetwork(
        page: Int,
        network: NetworkType,
        sort: SortType
    )
            : NetworkTvShowsResponse {
        return api.getPagedTvShowsWithNetwork(page, network.id, sort.queryParameter)
    }

    private suspend fun getPagedTvShowsWithGenre(
        page: Int,
        genre: GenreType,
        sort: SortType
    )
            : NetworkTvShowsResponse {
            return api.getPagedTvShowsWithGenre(page, genre.id, sort.queryParameter)
    }

    private suspend fun getPagedTvShowsWithGenreAndNetwork(
        page: Int,
        genre: GenreType,
        network: NetworkType,
        sort: SortType
    )
            : NetworkTvShowsResponse {
            return api.getPagedTvShowsWithGenreAndNetwork(page, genre.id, network.id, sort.queryParameter)
    }
}