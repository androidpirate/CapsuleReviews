package com.github.androidpirate.capsulereviews.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.androidpirate.capsulereviews.data.datasource.PagedTvShowsDataSourceFactory
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
        private val api: MovieDbService) {

    private val _showcaseTvShow = MutableLiveData<NetworkTvShow>()
    val showcaseTvShow: LiveData<NetworkTvShow>
    get() = _showcaseTvShow
    private val _showcaseVideoKey = MutableLiveData<String>()
    val showcaseVideoKey: LiveData<String>
    get() = _showcaseVideoKey

    fun getPagedTvShows(
        scope: CoroutineScope,
        genericSort: GenericSortType,
        sort: SortType,
        network: NetworkType,
        genre: GenreType
    ): LiveData<PagedList<NetworkTvShowsListItem?>> {
        val tvShowsDataSourceFactory = PagedTvShowsDataSourceFactory(api, scope, genericSort, sort, network, genre)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constants.PAGE_SIZE)
            .build()
        return LivePagedListBuilder<Int, NetworkTvShowsListItem>(tvShowsDataSourceFactory, config).build()
    }

    suspend fun fetchPopularTvShows(): List<NetworkTvShowsListItem> {
        val popularTvShows: List<NetworkTvShowsListItem>
        try {
            popularTvShows = api.getPopularTvShows().networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return popularTvShows
    }

    suspend fun fetchTopRatedTvShows(): List<NetworkTvShowsListItem> {
        val topRatedTvShows: List<NetworkTvShowsListItem>
        try {
            topRatedTvShows = api.getTopRatedTvShows().networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return topRatedTvShows
    }

    suspend fun fetchTrendingTvShows(): List<NetworkTvShowsListItem> {
        val trendingTvShows: List<NetworkTvShowsListItem>
        try {
            trendingTvShows = api.getTrendingTvShows().networkTvShowsListItems
            fetchShowcaseTvShow(trendingTvShows[0].id)
        } catch (e: NoConnectivityException) {
            throw e
        }
        return trendingTvShows
    }

    suspend fun fetchPopularTvShowsOnNetflix(): List<NetworkTvShowsListItem> {
        val popularTvShowsOnNetflix: List<NetworkTvShowsListItem>
        try {
            popularTvShowsOnNetflix =
                api.getPopularTvShowsOnNetwork(NetworkType.NETFLIX.id)
                    .networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return popularTvShowsOnNetflix
    }

    suspend fun fetchPopularTvShowsOnHulu(): List<NetworkTvShowsListItem> {
        val popularTvShowsOnHulu: List<NetworkTvShowsListItem>
        try {
            popularTvShowsOnHulu =
                api.getPopularTvShowsOnNetwork(NetworkType.HULU.id)
                    .networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return popularTvShowsOnHulu
    }

    suspend fun fetchPopularTvShowsOnDisneyPlus(): List<NetworkTvShowsListItem> {
        val popularTvShowsOnDisneyPlus: List<NetworkTvShowsListItem>
        try {
            popularTvShowsOnDisneyPlus =
                api.getPopularTvShowsOnNetwork(NetworkType.DISNEY_PLUS.id)
                    .networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return popularTvShowsOnDisneyPlus
    }

    suspend fun fetchTvShowDetails(showId: Int) : NetworkTvShow {
        val tvShowDetails: NetworkTvShow
        try {
            tvShowDetails = api.getTvShowDetails(showId)
        } catch (e: NoConnectivityException) {
            throw e
        }
        return tvShowDetails
    }

    suspend fun fetchSimilarTvShows(showId: Int): List<NetworkTvShowsListItem> {
        val similarTvShows: List<NetworkTvShowsListItem>
        try {
            similarTvShows = api.getSimilarTvShows(showId).networkTvShowsListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return similarTvShows
    }

    suspend fun fetchShowVideos(showId: Int) : List<NetworkVideosListItem> {
        val tvShowVideos: List<NetworkVideosListItem>
        try {
            tvShowVideos = api.getTvShowVideos(showId).networkVideosListItems
        } catch (e: NoConnectivityException) {
            throw e
        }
        return tvShowVideos
    }

    suspend fun fetchIMDBId(showId: Int): String {
        val imdbId: String
        try {
            imdbId = api.getTvShowExternalIDs(showId).imdbId
        } catch (e: NoConnectivityException) {
            throw e
        }
        return imdbId
    }

    fun getTvShowVideoKey(tvShowVideos: List<NetworkVideosListItem>): String {
        var videoKey = Constants.EMPTY_VIDEO_KEY
        if(tvShowVideos.isNotEmpty()) {
            for (video in tvShowVideos) {
                if (video.site == Constants.VIDEO_SITE && video.type == Constants.VIDEO_TYPE) {
                    videoKey = video.key
                    break
                }
            }
        }
        return videoKey
    }

    private suspend fun fetchShowcaseTvShow(showcaseTvShowId: Int) {
        val showcaseTvShow = fetchTvShowDetails(showcaseTvShowId)
        _showcaseTvShow.postValue(showcaseTvShow)
        val showcaseTvShowVideos = fetchShowVideos(showcaseTvShowId)
        _showcaseVideoKey.postValue(getTvShowVideoKey(showcaseTvShowVideos))
    }
}