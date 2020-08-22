package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import com.github.androidpirate.capsulereviews.data.db.MovieListDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
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

    suspend fun fetchAndPersistPopularMovies() {
        val popularMovies = api.getPopularMovies().networkMoviesListItems
        popularMovies.forEach {
            if(dao.isRowIsExist(it.id)) {
                dao.updatePopularMovie(it.id)
            } else {
                dao.insertPopularMovie(it.toPopularMovie())
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
    }

    suspend fun getShowCaseVideoKey(movieId: Int): String {
        var videoKey = ""
        val showcaseVideos = api.getMovieVideos(movieId).networkVideosListItems
        for(video in showcaseVideos) {
            if(video.site == "YouTube" && video.type == "Trailer") {
                videoKey = video.key
                break
            }
        }
        return videoKey
    }
}