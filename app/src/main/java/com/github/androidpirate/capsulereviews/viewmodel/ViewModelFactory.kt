package com.github.androidpirate.capsulereviews.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.androidpirate.capsulereviews.data.db.AppDatabase
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import java.lang.IllegalStateException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val application: Application): ViewModelProvider.Factory {

    private val repo = MoviesRepository(
        MovieDbService.invoke(),
        AppDatabase.invoke(application.applicationContext).movieListDao())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieListViewModel::class.java) -> {
                MovieListViewModel(repo) as T
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                MovieDetailViewModel(repo) as T
            }
            else -> throw IllegalStateException("No such view model class.")
        }
    }
}