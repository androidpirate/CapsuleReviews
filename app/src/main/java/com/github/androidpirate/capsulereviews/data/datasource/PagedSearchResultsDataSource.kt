package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.PageKeyedDataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchListItem
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchResponse
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.NoConnectivityException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagedSearchResultsDataSource(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val queryString: String): PageKeyedDataSource<Int, NetworkMultiSearchListItem>() {

    private var page = Constants.FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NetworkMultiSearchListItem>) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val response: NetworkMultiSearchResponse? = try {
                        getSearchResults(page, queryString)
                    } catch (e: NoConnectivityException) {
                        null
                    }

                    if(response != null) {
                        callback.onResult(
                            response.networkMultiSearchListItems,
                            null,
                            page + Constants.PAGE_LOAD_INCREMENT
                        )
                    } else {
                        // If load failed, return a list of nulls of length Constants.PAGE_SIZE:
                        callback.onResult(
                            MutableList<NetworkMultiSearchListItem?>(Constants.PAGE_SIZE) { null },
                            null,
                            page + Constants.PAGE_LOAD_INCREMENT)
                    }

                }
            }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMultiSearchListItem>) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val response: NetworkMultiSearchResponse? = try {
                        getSearchResults(params.key, queryString)
                    } catch (e: NoConnectivityException) {
                        null
                    }

                    if(response != null) {
                        if(response.totalPages >= params.key) {
                            callback.onResult(
                                response.networkMultiSearchListItems,
                                params.key + Constants.PAGE_LOAD_INCREMENT
                            )
                        }
                    } else {
                        // If load failed, return a list of nulls of length Constants.PAGE_SIZE:
                        callback.onResult(
                            MutableList<NetworkMultiSearchListItem?>(Constants.PAGE_SIZE) { null },
                            params.key + Constants.PAGE_LOAD_INCREMENT)
                    }

                }
            }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NetworkMultiSearchListItem>) {
        // Not used
    }

    private suspend fun getSearchResults(
        page: Int,
        queryString: String): NetworkMultiSearchResponse {
            return api.getSearchResults(page, queryString)
    }
}