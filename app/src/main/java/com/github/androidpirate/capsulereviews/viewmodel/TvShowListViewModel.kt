package com.github.androidpirate.capsulereviews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TvShowListViewModel(private val repo: TvShowsRepository): ViewModel() {

    val popularTvShows = repo.getPopularTvShows()
    val topRatedTvShows = repo.getTopRatedTvShows()
    val trendingTvShows = repo.getTrendingTvShows()
    val popularOnNetflix = repo.getPopularTvShowsOnNetflix()
    val popularOnHulu = repo.getPopularTvShowsOnHulu()
    val popularOnDisneyPlus = repo.getPopularTvShowsOnDisneyPlus()
    val showcaseTvShow = repo.getShowcaseTvShow()

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
}