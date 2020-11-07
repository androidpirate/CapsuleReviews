package com.github.androidpirate.capsulereviews.data.network.api

import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesResponse
import com.github.androidpirate.capsulereviews.data.network.response.multiSearch.NetworkMultiSearchResponse
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.external_ids.NetworkTvShowExternalIDs
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsResponse
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbService {
    /******************************
     *    Movies Endpoints
     ******************************/
    @GET("movie/popular")
    suspend fun getPopularMovies(): NetworkMoviesResponse

    @GET("movie/popular")
    suspend fun getPagedPopularMovies(
        @Query("page") page: Int): NetworkMoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): NetworkMoviesResponse

    @GET("movie/top_rated")
    suspend fun getPagedTopRatedMovies(
        @Query("page") page: Int): NetworkMoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("sort_by") sort: String): NetworkMoviesResponse

    @GET("movie/now_playing")
    suspend fun getPagedNowPlayingMovies(
        @Query("page") page: Int,
        @Query("sort_by") sort: String): NetworkMoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("sort_by") sort: String): NetworkMoviesResponse

    @GET("movie/upcoming")
    suspend fun getPagedUpcomingMovies(
        @Query("page") page: Int,
        @Query("sort_by") sort: String): NetworkMoviesResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(): NetworkMoviesResponse

    @GET("trending/movie/week")
    suspend fun getPagedTrendingMovies(
        @Query("page") page: Int): NetworkMoviesResponse

    @GET("discover/movie")
    suspend fun getPagedMoviesWithGenre(
        @Query("page")page: Int,
        @Query("with_genres") genre: Int,
        @Query("sort_by") sort: String): NetworkMoviesResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int): NetworkMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int): NetworkMovie

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int): NetworkVideosResponse

    /******************************
     *    Tv Shows Endpoints
     ******************************/
    @GET("tv/popular")
    suspend fun getPopularTvShows(): NetworkTvShowsResponse

    @GET("tv/popular")
    suspend fun getPagedPopularTvShows(
        @Query("page") page: Int): NetworkTvShowsResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(): NetworkTvShowsResponse

    @GET("tv/top_rated")
    suspend fun getPagedTopRatedTvShows(
        @Query("page") page: Int): NetworkTvShowsResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(): NetworkTvShowsResponse

    @GET("trending/tv/week")
    suspend fun getPagedTrendingTvShows(
        @Query("page") page: Int): NetworkTvShowsResponse

    @GET("discover/tv")
    suspend fun getPopularTvShowsOnNetwork(
        @Query("with_networks") networkId: Int): NetworkTvShowsResponse

    @GET("discover/tv")
    suspend fun getPagedTvShowsWithNetwork(
        @Query("page") page: Int,
        @Query("with_networks") networkId: Int,
        @Query("sort_by") sort: String): NetworkTvShowsResponse

    @GET("discover/tv")
    suspend fun getPagedTvShowsWithGenre(
        @Query("page") page: Int,
        @Query("with_genres") genre: Int,
        @Query("sort_by") sort: String): NetworkTvShowsResponse

    @GET("discover/tv")
    suspend fun getPagedTvShowsWithGenreAndNetwork(
        @Query("page") page: Int,
        @Query("with_genres") genre: Int,
        @Query("with_networks") networkId: Int,
        @Query("sort_by") sort: String): NetworkTvShowsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTvShows(
        @Path("tv_id") tvId: Int): NetworkTvShowsResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int): NetworkTvShow

    @GET("tv/{tv_id}/videos")
    suspend fun getTvShowVideos(
        @Path("tv_id") tvId: Int): NetworkVideosResponse

    @GET("tv/{tv_id}/external_ids")
    suspend fun getTvShowExternalIDs(
        @Path("tv_id") tvId: Int) : NetworkTvShowExternalIDs

    /******************************
     *    Multi Search Endpoints
     ******************************/
    @GET("search/multi")
    suspend fun getSearchResults(
        @Query("page") page: Int,
        @Query("query") query: String): NetworkMultiSearchResponse
}