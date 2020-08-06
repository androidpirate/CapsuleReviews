package com.github.androidpirate.capsulereviews.data.api

import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MovieDbService {
    // Popular Movies
    // https://api.themoviedb.org/3/movie/popular?api_key=16c6cda19ab9c4b72bfc817f9dadcc23&page={page_no}
    @GET("movie/popular")
    suspend fun getPopularMovies(): MoviesResponse
    // Top Rated Movies
    // https://api.themoviedb.org/3/movie/top_rated?api_key=16c6cda19ab9c4b72bfc817f9dadcc23&page={page_no}
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): MoviesResponse
    // Now Playing Movies
    // https://api.themoviedb.org/3/movie/now_playing?api_key=16c6cda19ab9c4b72bfc817f9dadcc23&page={page_no}
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): MoviesResponse
    // Upcoming Movies
    // https://api.themoviedb.org/3/movie/upcoming?api_key=16c6cda19ab9c4b72bfc817f9dadcc23&page={page_no}
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): MoviesResponse
    // Movie Details
    // https://api.themoviedb.org/3/movie/{movie_id}?api_key=16c6cda19ab9c4b72bfc817f9dadcc23
    // Movie Trailers Endpoint
    // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=16c6cda19ab9c4b72bfc817f9dadcc23
    // https://www.youtube.com/watch?v={key}
    // Get Similar Movies
    // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=16c6cda19ab9c4b72bfc817f9dadcc23
    companion object {
        operator fun invoke(): MovieDbService {
            val requestInterceptor = Interceptor { chain ->
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
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieDbService::class.java)
        }
    }
}