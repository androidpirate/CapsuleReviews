package com.github.androidpirate.capsulereviews.data.network.response.tvShow


import com.google.gson.annotations.SerializedName

data class NetworkNetworkInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)