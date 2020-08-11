package com.github.androidpirate.capsulereviews.data.network.response.tvShows


import com.google.gson.annotations.SerializedName

data class TvShowsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val tvShowsListItems: List<TvShowsListItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)