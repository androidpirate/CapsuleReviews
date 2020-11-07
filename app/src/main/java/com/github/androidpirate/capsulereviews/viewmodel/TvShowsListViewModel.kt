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

class TvShowsListViewModel
    @ViewModelInject
    constructor(
    private val repo: TvShowsRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    private val _popularTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    val popularTvShows: LiveData<List<NetworkTvShowsListItem>>
    get() = _popularTvShows

    private val _topRatedTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    val topRatedTvShows: LiveData<List<NetworkTvShowsListItem>>
    get() = _topRatedTvShows

    private val _trendingTvShows = MutableLiveData<List<NetworkTvShowsListItem>>()
    val trendingTvShows: LiveData<List<NetworkTvShowsListItem>>
    get() = _trendingTvShows

    private val _popularShowsOnNetflix = MutableLiveData<List<NetworkTvShowsListItem>>()
    val popularShowsOnNetflix: LiveData<List<NetworkTvShowsListItem>>
    get() = _popularShowsOnNetflix

    private val _popularShowsOnHulu = MutableLiveData<List<NetworkTvShowsListItem>>()
    val popularShowsOnHulu: LiveData<List<NetworkTvShowsListItem>>
    get() = _popularShowsOnHulu

    private val _popularShowsOnDisneyPlus = MutableLiveData<List<NetworkTvShowsListItem>>()
    val popularShowsOnDisneyPlus: LiveData<List<NetworkTvShowsListItem>>
    get() = _popularShowsOnDisneyPlus

    val showcaseTvShow = repo.showcaseTvShow
    val showcaseVideoKey = repo.showcaseVideoKey

    private val _isOnline = MutableLiveData<Boolean>(true)
    val isOnline: LiveData<Boolean>
    get() = _isOnline

    private var isShowcaseFavorite = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _popularTvShows.postValue(repo.fetchPopularTvShows())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _topRatedTvShows.postValue(repo.fetchTopRatedTvShows())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _trendingTvShows.postValue(repo.fetchTrendingTvShows())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _popularShowsOnNetflix.postValue(repo.fetchPopularTvShowsOnNetflix())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _popularShowsOnHulu.postValue(repo.fetchPopularTvShowsOnHulu())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    _popularShowsOnDisneyPlus.postValue(repo.fetchPopularTvShowsOnDisneyPlus())
                } catch (e: NoConnectivityException) {
                    setOffline()
                }
            }
        }
    }

    fun getIsShowcaseFavorite(): LiveData<Boolean> {
        return isShowcaseFavorite
    }

    fun setIsShowcaseFavorite(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isShowcaseFavorite.postValue(favRepo.isFavorite(itemId))
            }
        }
    }

    fun insertShowcaseMovieToFavorites(showcaseTvShow: NetworkTvShow) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseTvShow = showcaseTvShow.toFavorite()
                favRepo.insertFavorite(favoriteShowcaseTvShow)
                isShowcaseFavorite.postValue(true)
            }
        }
    }

    fun deleteShowcaseMovieFromFavorites(showcaseTvShow: NetworkTvShow) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = showcaseTvShow.toFavorite()
                favRepo.deleteFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(false)
            }
        }
    }

    private fun setOffline() {
        _isOnline.postValue(false)
    }
}