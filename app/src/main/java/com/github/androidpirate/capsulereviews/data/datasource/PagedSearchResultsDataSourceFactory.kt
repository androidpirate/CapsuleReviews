package com.github.androidpirate.capsulereviews.data.datasource

import androidx.paging.DataSource
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchListItem
import kotlinx.coroutines.CoroutineScope

class PagedSearchResultsDataSourceFactory(
    private val api: MovieDbService,
    private val scope: CoroutineScope,
    private val queryString: String
): DataSource.Factory<Int, NetworkMultiSearchListItem>() {

    override fun create(): DataSource<Int, NetworkMultiSearchListItem> {
        return PagedSearchResultsDataSource(api, scope, queryString)
    }
}