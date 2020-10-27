package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.datasource.PagedSearchResultsDataSourceFactory
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchListItem
import com.github.androidpirate.capsulereviews.util.internal.Constants
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SearchRepository
    @Inject
    constructor(private val api: MovieDbService) {

    fun getSearchResults(
        scope: CoroutineScope,
        queryString: String): LiveData<PagedList<NetworkMultiSearchListItem>> {
        val searchDataSourceFactory = PagedSearchResultsDataSourceFactory(api, scope, queryString)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constants.PAGE_SIZE)
            .build()
        return LivePagedListBuilder<Int, NetworkMultiSearchListItem>(
            searchDataSourceFactory,
            config)
            .build()
    }
}