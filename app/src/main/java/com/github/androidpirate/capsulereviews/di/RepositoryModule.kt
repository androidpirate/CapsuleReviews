package com.github.androidpirate.capsulereviews.di

import com.github.androidpirate.capsulereviews.data.db.FavoritesDao
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.repo.FavoritesRepository
import com.github.androidpirate.capsulereviews.data.repo.MoviesRepository
import com.github.androidpirate.capsulereviews.data.repo.SearchRepository
import com.github.androidpirate.capsulereviews.data.repo.TvShowsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(service: MovieDbService) =
        MoviesRepository(service)

    @Provides
    @Singleton
    fun provideFavoritesRepository(dao: FavoritesDao) =
        FavoritesRepository(dao)

    @Provides
    @Singleton
    fun provideSearchRepository(service: MovieDbService) =
        SearchRepository(service)

    @Provides
    @Singleton
    fun provideTvShowsRepository(service: MovieDbService) =
        TvShowsRepository(service)
}