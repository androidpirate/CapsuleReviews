package com.github.androidpirate.capsulereviews.data.network.response.movies


import com.google.gson.annotations.SerializedName

data class NetworkMoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val networkMoviesListItems: List<NetworkMoviesListItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)