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
import com.github.androidpirate.capsulereviews.util.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvShowDetailViewModel
    @ViewModelInject
    constructor(
    private val repo: TvShowsRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val _tvShowDetails = MutableLiveData<NetworkTvShow>()
    private val tvShowDetails: LiveData<NetworkTvShow>
    get() = _tvShowDetails

    private val _similarTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    private val similarTvShows: LiveData<List<NetworkTvShowsListItem>>
    get() = _similarTvShows

    private val _tvShowVideoKey = MutableLiveData<String>()
    private val tvShowVideoKey: LiveData<String>
    get() = _tvShowVideoKey

    private val _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean>
    get() = _isOnline

    private var imdbId = MutableLiveData<String>()
    private var imdbEndpoint = MutableLiveData<String>()
    private var isFavorite = MutableLiveData<Boolean>()
    private var flagDecoration = false

    private fun setOffline() {
        _isOnline.postValue(false)
    }

    fun getTvShowDetails(showId: Int): LiveData<NetworkTvShow> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val networkTvShow = repo.fetchTvShowDetails(showId)
                    _tvShowDetails.postValue(networkTvShow)
                    setIsFavorite(networkTvShow.id)
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return tvShowDetails
    }

    fun getSimilarTvShows(showId: Int): LiveData<List<NetworkTvShowsListItem>> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _similarTvShows.postValue(repo.fetchSimilarTvShows(showId))
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return similarTvShows
    }

    fun getShowKey(showId: Int): LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val tvShowVideos = repo.fetchShowVideos(showId)
                    _tvShowVideoKey.postValue(repo.getTvShowVideoKey(tvShowVideos))
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return tvShowVideoKey
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

    fun getIMDBId(showId: Int) : LiveData<String> {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    imdbId.postValue(repo.fetchIMDBId(showId))
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
        return imdbId
    }

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