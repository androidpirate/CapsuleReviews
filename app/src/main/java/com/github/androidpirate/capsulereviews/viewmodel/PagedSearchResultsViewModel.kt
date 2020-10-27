package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchListItem
import com.github.androidpirate.capsulereviews.data.repo.SearchRepository
import com.github.androidpirate.capsulereviews.util.internal.Constants

class PagedSearchResultsViewModel
    @ViewModelInject
    constructor(
    private val repo: SearchRepository): ViewModel() {

    val queryString = MutableLiveData<String>(Constants.EMPTY_FIELD_STRING)

    fun setQueryString(query: String) = apply { queryString.value = query }

    val searchResults: LiveData<PagedList<NetworkMultiSearchListItem>> =
        Transformations.switchMap(queryString, ::getSearchResults)

    private fun getSearchResults(queryString: String) = repo.getSearchResults(
        scope = viewModelScope,
        queryString = queryString
    )

    private var flagDecoration = false

    fun setFlagDecorationOn() {
        flagDecoration = true
    }

    fun setFlagDecorationOff() {
        flagDecoration = false
    }

    fun getFlagDecoration(): Boolean {
        return flagDecoration
    }
}