package com.github.androidpirate.capsulereviews.data.api

import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.network.response.movie.MovieResponse
import com.github.androidpirate.capsulereviews.data.network.response.movies.MoviesResponse
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.MultiSearchResponse
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.TvShowResponse
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.external_ids.TvShowExternalIDs
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.TvShowsResponse
import com.github.androidpirate.capsulereviews.data.network.response.videos.VideosResponse
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
    suspend fun getPopularMovies(): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): MoviesResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(): MoviesResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: Int): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): VideosResponse

    /******************************
     *    Tv Shows Endpoints
     ******************************/
    @GET("tv/popular")
    suspend fun getPopularTvShows(): TvShowsResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(): TvShowsResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(): TvShowsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(@Path("tv_id") tvId: Int): TvShowsResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(@Path("tv_id") tvId: Int): TvShowResponse

    @GET("tv/{tv_id}/videos")
    suspend fun getTvShowVideos(@Path("tv_id") tvId: Int): VideosResponse

    @GET("tv/{tv_id}/external_ids")
    suspend fun getTvShowExternalIDs(@Path("tv_id") tvId: Int) : TvShowExternalIDs

    /******************************
     *    Multi Search Endpoints
     ******************************/
    @GET("search/multi")
    suspend fun getSearchResults(@Query("query") query: String): MultiSearchResponse

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