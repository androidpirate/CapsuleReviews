package com.github.androidpirate.capsulereviews.data.response.videos


import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val videosListItems: List<VideosListItem>
)