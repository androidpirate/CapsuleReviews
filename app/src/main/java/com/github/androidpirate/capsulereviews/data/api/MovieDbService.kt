package com.github.androidpirate.capsulereviews.data.api

import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.response.movie.MovieResponse
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesResponse
import com.github.androidpirate.capsulereviews.data.response.tvShow.TvShowResponse
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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

    // Movie Trailers Endpoint
    // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=16c6cda19ab9c4b72bfc817f9dadcc23
    // https://www.youtube.com/watch?v={key}
    // TODO 3: Videos require a VideosResponseType

    /******************************
     *    Tv Shows Endpoints
     ******************************/
    @GET("tv/popular")
    suspend fun getPopularTvShows(): TvShowsResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(): TvShowsResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(): TvShowsResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(@Path("tv_id") tvId: Int): TvShowResponse

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