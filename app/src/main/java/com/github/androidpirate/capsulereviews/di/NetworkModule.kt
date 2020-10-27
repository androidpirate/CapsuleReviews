package com.github.androidpirate.capsulereviews.di

import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideBaseURL():String = BuildConfig.MOVIE_DB_BASE_URL

    @Provides
    fun provideRequestInterceptor() = Interceptor { chain ->
        val updatedUrl = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_TOKEN)
            .build()
        val updatedRequest = chain.request()
            .newBuilder()
            .url(updatedUrl)
            .build()
        return@Interceptor chain.proceed(updatedRequest)
    }

    @Provides
    @Singleton
    fun provideOkHttp(requestInterceptor: Interceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMovieDbService(retrofit: Retrofit): MovieDbService =
        retrofit.create(MovieDbService::class.java)
}