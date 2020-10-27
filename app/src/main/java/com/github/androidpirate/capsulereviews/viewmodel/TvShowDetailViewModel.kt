package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvShowDetailViewModel
    @ViewModelInject
    constructor(
    private val repo: TvShowsRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val tvShowDetails = MutableLiveData<NetworkTvShow>()
    private val tvShowVideoKey = MutableLiveData<String>()
    private val similarTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    private val imdbId = MutableLiveData<String> ()
    private var imdbEndpoint = MutableLiveData<String>()
    private var isFavorite = MutableLiveData<Boolean>()

    fun getTvShowDetails(showId: Int): LiveData<NetworkTvShow> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val networkTvShow = repo.fetchTvShowDetails(showId)
                tvShowDetails.postValue(networkTvShow)
                setIsFavorite(networkTvShow.id)
            }
        }
        return tvShowDetails
    }

    fun getIsFavorite(): LiveData<Boolean> {
        return isFavorite
    }

    private fun setIsFavorite(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isFavorite.postValue(favRepo.isFavorite(itemId))
            }
        }
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

    fun insertFavoriteTvShow(tvShow: NetworkTvShow) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favRepo.insertFavorite(tvShow.toFavorite())
                isFavorite.postValue(true)
            }
        }
    }

    fun deleteFavoriteTvShow(tvShow: NetworkTvShow) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favRepo.deleteFavorite(tvShow.toFavorite())
                isFavorite.postValue(false)
            }
        }
    }

    fun setImdbEndpoint(imdbEndpoint: String) {
        this.imdbEndpoint.apply { value  = imdbEndpoint}
    }

    fun getImdbEndpoint(): LiveData<String> {
        return imdbEndpoint
    }

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