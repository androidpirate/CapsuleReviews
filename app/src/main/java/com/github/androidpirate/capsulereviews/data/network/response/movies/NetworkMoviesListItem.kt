package com.github.androidpirate.capsulereviews.data.network.response.movies


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

class NetworkMoviesListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double
)