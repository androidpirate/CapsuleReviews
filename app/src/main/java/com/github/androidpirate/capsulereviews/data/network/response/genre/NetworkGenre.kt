package com.github.androidpirate.capsulereviews.data.network.response.genre


import com.google.gson.annotations.SerializedName

data class NetworkGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)