package com.github.androidpirate.capsulereviews.data.network.response.multiSearch


import com.google.gson.annotations.SerializedName

data class NetworkMultiSearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val networkMultiSearchListItems: List<NetworkMultiSearchListItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)