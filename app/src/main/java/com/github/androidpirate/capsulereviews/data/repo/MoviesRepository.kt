package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.datasource.PagedMoviesDataSourceFactory
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem
import com.github.androidpirate.capsulereviews.util.internal.*
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MoviesRepository
    @Inject
    constructor(
        private val api: MovieDbService) {

    private val _showcaseMovie = MutableLiveData<NetworkMovie>()
    val showcaseMovie: LiveData<NetworkMovie>
    get() = _showcaseMovie
    private val _showcaseVideoKey = MutableLiveData<String>()
    val showcaseVideoKey: LiveData<String>
    get() = _showcaseVideoKey

    fun getPagedMovies(
        scope: CoroutineScope,
        genericSort: GenericSortType,
        sort: SortType,
        genre: GenreType)
        : LiveData<PagedList<NetworkMoviesListItem?>> {
             val moviesDataSourceFactory = PagedMoviesDataSourceFactory(api, scope, genericSort, sort, genre)
             val config =
                 PagedList.Config.Builder()
                     .setEnablePlaceholders(false)
                     .setPageSize(Constants.PAGE_SIZE)
                     .build()
             return LivePagedListBuilder<Int, NetworkMoviesListItem>(moviesDataSourceFactory, config).build()
    }

    suspend fun fetchPopularMovies(): List<NetworkMoviesListItem> {
        val popularMovies: List<NetworkMoviesListItem>
        try {
            popularMovies = api.getPopularMovies().networkMoviesListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return popularMovies
    }

    suspend fun fetchTopRatedMovies(): List<NetworkMoviesListItem> {
        val topRatedMovies: List<NetworkMoviesListItem>
        try {
            topRatedMovies = api.getTopRatedMovies().networkMoviesListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return topRatedMovies
    }

    suspend fun fetchNowPlayingMovies(): List<NetworkMoviesListItem> {
        val nowPlayingMovies: List<NetworkMoviesListItem>
        try {
            nowPlayingMovies = api.getNowPlayingMovies(BuildConfig.RELEASE_DATE_DESC).networkMoviesListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return nowPlayingMovies
    }

    suspend fun fetchUpcomingMovies(): List<NetworkMoviesListItem> {
        val upcomingMovies: List<NetworkMoviesListItem>
        try {
            upcomingMovies = api.getUpcomingMovies(BuildConfig.RELEASE_DATE_DESC).networkMoviesListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return upcomingMovies
    }

    suspend fun fetchTrendingMovies(): List<NetworkMoviesListItem> {
        val trendingMovies: List<NetworkMoviesListItem>
        try {
            trendingMovies = api.getTrendingMovies().networkMoviesListItems
            fetchShowcaseMovie(trendingMovies[0].id)
        } catch (e: NoConnectivityException) {
            throw e
        }
        return trendingMovies
    }

    suspend fun fetchMovieDetails(movieId: Int): NetworkMovie {
        val movieDetails: NetworkMovie
        try {
            movieDetails = api.getMovieDetails(movieId)
        } catch (e: NoConnectivityException) {
            throw e
        }
        return movieDetails
    }

    suspend fun fetchSimilarMovies(movieId: Int): List<NetworkMoviesListItem> {
        val similarMovies: List<NetworkMoviesListItem>
        try {
            similarMovies = api.getSimilarMovies(movieId).networkMoviesListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return similarMovies
    }

    suspend fun fetchMovieVideos(movieId: Int): List<NetworkVideosListItem> {
        val movieVideos: List<NetworkVideosListItem>
        try {
            movieVideos = api.getMovieVideos(movieId).networkVideosListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return movieVideos
    }

    fun getMovieVideoKey(movieVideos: List<NetworkVideosListItem>): String {
        var videoKey = Constants.EMPTY_VIDEO_KEY
        if(movieVideos.isNotEmpty()) {
            for (video in movieVideos) {
                if (video.site == Constants.VIDEO_SITE && video.type == Constants.VIDEO_TYPE) {
                    videoKey = video.key
                    break
                }
            }
        }
        return videoKey
    }

    private suspend fun fetchShowcaseMovie(showcaseMovieId: Int) {
        val showcaseMovie = fetchMovieDetails(showcaseMovieId)
        _showcaseMovie.postValue(showcaseMovie)
        val showcaseMovieVideos = fetchMovieVideos(showcaseMovieId)
        _showcaseVideoKey.postValue(getMovieVideoKey(showcaseMovieVideos))
    }
}