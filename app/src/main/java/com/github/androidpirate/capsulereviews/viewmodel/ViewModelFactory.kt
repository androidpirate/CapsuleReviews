package com.github.androidpirate.capsulereviews.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.androidpirate.capsulereviews.data.db.AppDatabase
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(application: Application): ViewModelProvider.Factory {
    private val movieDbService = MovieDbService.invoke()
    private val appDatabase = AppDatabase.invoke(application.applicationContext)
    private val moviesRepo = MoviesRepository(
        movieDbService,
        appDatabase.movieListDao())
    private val tvShowsRepo = TvShowsRepository(
        movieDbService,
        appDatabase.tvShowListDao()
    )

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MoviesListViewModel::class.java) -> {
                MoviesListViewModel(moviesRepo) as T
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                MovieDetailViewModel(moviesRepo) as T
            }
            modelClass.isAssignableFrom(TvShowsListViewModel::class.java) -> {
                TvShowsListViewModel(tvShowsRepo) as T
            }
            modelClass.isAssignableFrom(TvShowDetailViewModel::class.java) -> {
                TvShowDetailViewModel(tvShowsRepo) as T
            }
            modelClass.isAssignableFrom(PagedMoviesListViewModel::class.java) -> {
                PagedMoviesListViewModel(moviesRepo) as T
            }
            modelClass.isAssignableFrom(PagedTvShowsListViewModel::class.java) -> {
                PagedTvShowsListViewModel(tvShowsRepo) as T
            }
            else -> throw IllegalStateException("No such view model class.")
        }
    }
}