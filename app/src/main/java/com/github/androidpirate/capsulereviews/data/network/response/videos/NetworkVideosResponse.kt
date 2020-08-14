package com.github.androidpirate.capsulereviews.data.network.response.videos


import com.google.gson.annotations.SerializedName

data class NetworkVideosResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val networkVideosListItems: List<NetworkVideosListItem>
)