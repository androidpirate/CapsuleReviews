package com.github.androidpirate.capsulereviews.data.api

import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesResponse
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchResponse
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.external_ids.NetworkTvShowExternalIDs
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsResponse
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbService {
    /******************************
     *    Movies Endpoints
     ******************************/
    @GET("movie/popular")
    suspend fun getPopularMovies(): NetworkMoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): NetworkMoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): NetworkMoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): NetworkMoviesResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(): NetworkMoviesResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: Int): NetworkMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): NetworkMovie

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): NetworkVideosResponse

    /******************************
     *    Tv Shows Endpoints
     ******************************/
    @GET("tv/popular")
    suspend fun getPopularTvShows(): NetworkTvShowsResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(): NetworkTvShowsResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(): NetworkTvShowsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(@Path("tv_id") tvId: Int): NetworkTvShowsResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(@Path("tv_id") tvId: Int): NetworkTvShow

    @GET("tv/{tv_id}/videos")
    suspend fun getTvShowVideos(@Path("tv_id") tvId: Int): NetworkVideosResponse

    @GET("tv/{tv_id}/external_ids")
    suspend fun getTvShowExternalIDs(@Path("tv_id") tvId: Int) : NetworkTvShowExternalIDs

    /******************************
     *    Multi Search Endpoints
     ******************************/
    @GET("search/multi")
    suspend fun getSearchResults(@Query("query") query: String): NetworkMultiSearchResponse

    companion object {
        operator fun invoke(): MovieDbService {
            val requestInterceptor = Interceptor { chain ->
                val updatedUrl = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_TOKEN)
                    .addQueryParameter("language", BuildConfig.MOVIE_DB_LANG_VALUE)
                    .addQueryParameter("with_original_language", BuildConfig.MOVIE_DB_ORIG_LANG_VALUE)
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