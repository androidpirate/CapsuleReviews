package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvShowDetailViewModel(private val repo: TvShowsRepository): ViewModel() {

    private val tvShowDetails = MutableLiveData<NetworkTvShow>()
    private val tvShowVideoKey = MutableLiveData<String>()
    private val similarTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    private val imdbId = MutableLiveData<String> ()

    fun getTvShowDetails(showId: Int): LiveData<NetworkTvShow> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvShowDetails.postValue(repo.fetchTvShowDetails(showId))
            }
        }
        return tvShowDetails
    }

    fun getSimilarTvShows(showId: Int): LiveData<List<NetworkTvShowsListItem>> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                similarTvShows.postValue(repo.fetchSimilarTvShows(showId))
            }
        }
        return similarTvShows
    }

    fun getShowKey(showId: Int): LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tvShowVideoKey.postValue(repo.fetchVideoKey(showId))
            }
        }
        return tvShowVideoKey
    }

    fun getIMDBId(showId: Int) : LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                imdbId.postValue(repo.fetchIMDBId(showId))
            }
        }
        return imdbId
    }

}