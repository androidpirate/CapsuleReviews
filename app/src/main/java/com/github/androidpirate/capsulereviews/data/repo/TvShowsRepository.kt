package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import com.github.androidpirate.capsulereviews.data.db.TvShowListDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem

class TvShowsRepository(
    private val api: MovieDbService,
    private val dao: TvShowListDao
) {
    fun getPopularTvShows(): LiveData<List<DBTvShow>> {
        return dao.getPopularTvShows()
    }

    fun getTopRatedTvShows(): LiveData<List<DBTvShow>> {
        return dao.getTopRatedTvShows()
    }

    fun getTrendingTvShows(): LiveData<List<DBTvShow>> {
        return dao.getTrendingTvShows()
    }

    suspend fun fetchAndPersistPopularTvShows() {
        val popularShows = api.getPopularTvShows().networkTvShowsListItems
        popularShows.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updatePopularTvShow(it.id)
            } else {
                dao.insertPopularTvShow(it.toPopular())
            }
        }
    }

    suspend fun fetchAndPersistTopRatedTvShows() {
        val topRatedShows = api.getTopRatedTvShows().networkTvShowsListItems
        topRatedShows.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updateTopRatedTvShow(it.id)
            } else {
                dao.insertTopRatedTvShow(it.toTopRated())
            }
        }
    }

    suspend fun fetchAndPersistTrendingTvShows() {
        val trendingShows = api.getTrendingTvShows().networkTvShowsListItems
        trendingShows.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updateTrendingTvShow(it.id)
            } else {
                dao.insertTrendingTvShow(it.toTrending())
            }
        }
    }

    suspend fun fetchVideoKey(showId: Int): String {
        val videos = fetchShowVideos(showId)
        return fetchShowVideoKey(videos)
    }

    private suspend fun fetchShowVideos(showId: Int) : List<NetworkVideosListItem> {
        return api.getTvShowVideos(showId).networkVideosListItems
    }

    private fun fetchShowVideoKey(videos: List<NetworkVideosListItem>): String {
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