package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import com.github.androidpirate.capsulereviews.data.db.MovieListDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem

class MoviesRepository(
    private val api: MovieDbService,
    private val dao: MovieListDao
) {

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
        val nowPlayingMovies = api.getNowPlayingMovies().networkMoviesListItems
        nowPlayingMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updateNowPlayingMovie(it.id)
            } else {
                dao.insertNowPlayingMovie(it.toNowPlaying())
            }
        }
    }

    suspend fun fetchAndPersistUpcomingMovies() {
        val upcomingMovies = api.getUpcomingMovies().networkMoviesListItems
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
        var videoKey = EMPTY_VIDEO_KEY
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

    companion object {
        const val EMPTY_VIDEO_KEY = ""
    }

}