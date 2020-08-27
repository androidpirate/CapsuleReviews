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
        }
    }
}