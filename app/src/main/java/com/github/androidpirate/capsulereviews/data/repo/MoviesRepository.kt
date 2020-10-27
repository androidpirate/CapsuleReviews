package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.data.datasource.PagedMoviesDataSourceFactory
import com.github.androidpirate.capsulereviews.data.db.MovieListDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.SortType
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MoviesRepository
    @Inject
    constructor(
        private val api: MovieDbService,
        private val dao: MovieListDao) {

    fun getPopularMovies(): LiveData<List<DBMovie>> {
        return dao.getPopularMovies()
    }

    fun getTopRatedMovies(): LiveData<List<DBMovie>> {
        return dao.getTopRatedMovies()
    }

    fun getNowPlayingMovies(): LiveData<List<DBMovie>> {
        return dao.getNowPlayingMovies()
    }

    fun getUpcomingMovies(): LiveData<List<DBMovie>> {
        return dao.getUpComingMovies()
    }

    fun getTrendingMovies(): LiveData<List<DBMovie>> {
        return dao.getTrendingMovies()
    }

    fun getShowcaseMovie(): LiveData<DBMovieShowcase> {
        return dao.getShowcaseMovie()
    }

    fun getPagedMovies(
        scope: CoroutineScope,
        genericSort: GenericSortType,
        sort: SortType,
        genre: GenreType)
        : LiveData<PagedList<NetworkMoviesListItem>> {
            val moviesDataSourceFactory = PagedMoviesDataSourceFactory(api, scope, genericSort, sort, genre)
            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(Constants.PAGE_SIZE)
                .build()
            return LivePagedListBuilder<Int, NetworkMoviesListItem>(
                moviesDataSourceFactory,
                config)
                .build()
    }

    suspend fun fetchAndPersistPopularMovies() {
        val popularMovies = api.getPopularMovies().networkMoviesListItems
        popularMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updatePopularMovie(it.id)
            } else {
                dao.insertPopularMovie(it.toPopular())
            }
        }
    }

    suspend fun fetchAndPersistTopRatedMovies() {
        val topRatedMovies = api.getTopRatedMovies().networkMoviesListItems
        topRatedMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updateTopRatedMovie(it.id)
            } else {
                dao.insertTopRatedMovie(it.toTopRated())
            }
        }
    }

    suspend fun fetchAndPersistNowPlayingMovies() {
        val nowPlayingMovies = api.getNowPlayingMovies(BuildConfig.RELEASE_DATE_DESC).networkMoviesListItems
        nowPlayingMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updateNowPlayingMovie(it.id)
            } else {
                dao.insertNowPlayingMovie(it.toNowPlaying())
            }
        }
    }

    suspend fun fetchAndPersistUpcomingMovies() {
        val upcomingMovies = api.getUpcomingMovies(BuildConfig.RELEASE_DATE_DESC).networkMoviesListItems
        upcomingMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updateUpcomingMovie(it.id)
            } else {
                dao.insertUpcomingMovie(it.toUpcoming())
            }
        }
    }

    suspend fun fetchAndPersistTrendingMovies() {
        val trendingMovies = api.getTrendingMovies().networkMoviesListItems
        trendingMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updateTrendingMovie(it.id)
            } else {
                dao.insertTrendingMovie(it.toTrending())
            }
        }
        persistShowcaseMovie(trendingMovies[0])
    }

    suspend fun fetchVideoKey(movieId: Int): String {
        val videos = fetchMovieVideos(movieId)
        return fetchMovieVideoKey(videos)
    }

    suspend fun fetchMovieDetails(movieId: Int): NetworkMovie {
        return api.getMovieDetails(movieId)
    }

    suspend fun fetchSimilarMovies(movieId: Int): List<NetworkMoviesListItem> {
        return api.getSimilarMovies(movieId).networkMoviesListItems
    }

    private suspend fun fetchMovieVideos(movieId: Int): List<NetworkVideosListItem> {
        return api.getMovieVideos(movieId).networkVideosListItems
    }

    private suspend fun persistShowcaseMovie(showcaseMovie: NetworkMoviesListItem) {
        val showcaseVideos = fetchMovieVideos(showcaseMovie.id)
        val dbShowcaseMovie = showcaseMovie.toShowcase()
        dbShowcaseMovie.videoKey = fetchMovieVideoKey(showcaseVideos)
        dao.insertShowcaseMovie(dbShowcaseMovie)
    }

    private fun fetchMovieVideoKey(videos: List<NetworkVideosListItem>): String {
        var videoKey = Constants.EMPTY_VIDEO_KEY
        if(videos.isNotEmpty()) {
            for (video in videos) {
                if (video.site == "YouTube" && video.type == "Trailer") {
                    videoKey = video.key
                    break
                }
            }
        }
        return videoKey
    }
}