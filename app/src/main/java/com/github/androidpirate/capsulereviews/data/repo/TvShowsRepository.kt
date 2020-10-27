package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.datasource.PagedTvShowsDataSourceFactory
import com.github.androidpirate.capsulereviews.data.db.TvShowListDao
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShowShowcase
import com.github.androidpirate.capsulereviews.data.network.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.data.network.response.videos.NetworkVideosListItem
import com.github.androidpirate.capsulereviews.util.internal.*
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TvShowsRepository
    @Inject
    constructor(
        private val api: MovieDbService,
        private val dao: TvShowListDao) {

    fun getPopularTvShows(): LiveData<List<DBTvShow>> {
        return dao.getPopularTvShows()
    }

    fun getTopRatedTvShows(): LiveData<List<DBTvShow>> {
        return dao.getTopRatedTvShows()
    }

    fun getTrendingTvShows(): LiveData<List<DBTvShow>> {
        return dao.getTrendingTvShows()
    }

    fun getPopularTvShowsOnNetflix(): LiveData<List<DBTvShow>> {
        return dao.getPopularShowsOnNetflix()
    }

    fun getPopularTvShowsOnHulu(): LiveData<List<DBTvShow>> {
        return dao.getPopularShowsOnHulu()
    }

    fun getPopularTvShowsOnDisneyPlus(): LiveData<List<DBTvShow>> {
        return dao.getPopularShowsOnDisneyPlus()
    }

    fun getShowcaseTvShow(): LiveData<DBTvShowShowcase> {
        return dao.getShowcaseTvShow()
    }

    fun getPagedTvShows(
        scope: CoroutineScope,
        genericSort: GenericSortType,
        sort: SortType,
        network: NetworkType,
        genre: GenreType
    ): LiveData<PagedList<NetworkTvShowsListItem>> {
        val tvShowsDataSourceFactory = PagedTvShowsDataSourceFactory(api, scope, genericSort, sort, network, genre)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constants.PAGE_SIZE)
            .build()
        return LivePagedListBuilder<Int, NetworkTvShowsListItem>(
            tvShowsDataSourceFactory,
            config
        ).build()
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
        persistShowcaseTvShow(trendingShows[0])
    }

    suspend fun fetchAndPersistPopularNetflixTvShows() {
        val popularShowsOnNetflix = api.getPopularTvShowsOnNetwork(NetworkType.NETFLIX.id).networkTvShowsListItems
        popularShowsOnNetflix.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updatePopularTvShowOnNetflix(it.id)
            } else {
                dao.insertPopularTvShowOnNetflix(it.toNetflix())
            }
        }
    }

    suspend fun fetchAndPersistPopularHuluTvShows() {
        val popularShowsOnHulu = api.getPopularTvShowsOnNetwork(NetworkType.HULU.id).networkTvShowsListItems
        popularShowsOnHulu.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updatePopularTvShowOnHulu(it.id)
            } else {
                dao.insertPopularTvShowOnHulu(it.toHulu())
            }
        }
    }

    suspend fun fetchAndPersisPopularDisneyPlusTvShows() {
        val popularShowsOnDisneyPlus = api.getPopularTvShowsOnNetwork(NetworkType.DISNEY_PLUS.id).networkTvShowsListItems
        popularShowsOnDisneyPlus.forEach {
            if(dao.isRowExist(it.id)) {
                dao.updatePopularTvShowOnDisneyPlus(it.id)
            } else {
                dao.insertPopularTvShowOnDisneyPlus(it.toDisneyPlus())
            }
        }
    }

    suspend fun fetchVideoKey(showId: Int): String {
        val videos = fetchShowVideos(showId)
        return fetchShowVideoKey(videos)
    }

    suspend fun fetchTvShowDetails(showId: Int) : NetworkTvShow {
        return api.getTvShowDetails(showId)
    }

    suspend fun fetchSimilarTvShows(showId: Int): List<NetworkTvShowsListItem> {
        return api.getSimilarTvShows(showId).networkTvShowsListItems
    }

    suspend fun fetchIMDBId(showId: Int): String {
        return api.getTvShowExternalIDs(showId).imdbId
    }

    private suspend fun fetchShowVideos(showId: Int) : List<NetworkVideosListItem> {
        return api.getTvShowVideos(showId).networkVideosListItems
    }

    private suspend fun persistShowcaseTvShow(showcaseTvShow: NetworkTvShowsListItem) {
        val showcaseVideos = fetchShowVideos(showcaseTvShow.id)
        val dbShowcaseTvShow = showcaseTvShow.toShowcase()
        dbShowcaseTvShow.videoKey = fetchShowVideoKey(showcaseVideos)
        dao.insertShowcaseTvShow(dbShowcaseTvShow)
    }

    private fun fetchShowVideoKey(videos: List<NetworkVideosListItem>): String {
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