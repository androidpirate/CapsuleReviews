package com.github.androidpirate.capsulereviews.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShowShowcase
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvShowsListViewModel
    @ViewModelInject
    constructor(
    private val repo: TvShowsRepository,
    private val favRepo: FavoritesRepository): ViewModel() {

    val popularTvShows = repo.getPopularTvShows()
    val topRatedTvShows = repo.getTopRatedTvShows()
    val trendingTvShows = repo.getTrendingTvShows()
    val popularOnNetflix = repo.getPopularTvShowsOnNetflix()
    val popularOnHulu = repo.getPopularTvShowsOnHulu()
    val popularOnDisneyPlus = repo.getPopularTvShowsOnDisneyPlus()
    val showcaseTvShow = repo.getShowcaseTvShow()
    private var isShowcaseFavorite = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistPopularTvShows()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistTopRatedTvShows()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistTrendingTvShows()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistPopularNetflixTvShows()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersistPopularHuluTvShows()
            }
            withContext(Dispatchers.IO) {
                repo.fetchAndPersisPopularDisneyPlusTvShows()
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

    fun insertShowcaseMovieToFavorites(showcaseTvShow: DBTvShowShowcase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseTvShow = repo.fetchTvShowDetails(showcaseTvShow.tvShowId).toFavorite()
                favRepo.insertFavorite(favoriteShowcaseTvShow)
                isShowcaseFavorite.postValue(true)
            }
        }
    }

    fun deleteShowcaseMovieFromFavorites(showcaseTvShow: DBTvShowShowcase) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteShowcaseMovie = repo.fetchTvShowDetails(showcaseTvShow.tvShowId).toFavorite()
                favRepo.deleteFavorite(favoriteShowcaseMovie)
                isShowcaseFavorite.postValue(false)
            }
        }
    }
}