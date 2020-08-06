package com.github.androidpirate.capsulereviews.data.response.tvShows


import com.google.gson.annotations.SerializedName

data class TvShows(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val tvShowsResults: List<TvShowsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)