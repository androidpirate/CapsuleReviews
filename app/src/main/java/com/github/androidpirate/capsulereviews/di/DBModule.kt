package com.github.androidpirate.capsulereviews.di

import android.content.Context
import com.github.androidpirate.capsulereviews.data.db.AppDatabase
import com.github.androidpirate.capsulereviews.data.db.FavoritesDao
import com.github.androidpirate.capsulereviews.data.db.MovieListDao
import com.github.androidpirate.capsulereviews.data.db.TvShowListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DBModule {

    @Provides
    @Singleton
    fun provideMovieListDao(@ApplicationContext appContext: Context): MovieListDao {
        return AppDatabase.invoke(appContext).movieListDao()
    }

    @Provides
    @Singleton
    fun provideTvListDao(@ApplicationContext appContext: Context): TvShowListDao {
        return AppDatabase.invoke(appContext).tvShowListDao()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(@ApplicationContext appContext: Context): FavoritesDao {
        return AppDatabase.invoke(appContext).favoritesDao()
    }
}