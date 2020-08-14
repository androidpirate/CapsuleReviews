package com.github.androidpirate.capsulereviews.data.network.response.tvShows


import com.google.gson.annotations.SerializedName

data class NetworkTvShowsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val networkTvShowsListItems: List<NetworkTvShowsListItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)