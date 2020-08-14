package com.github.androidpirate.capsulereviews.data.network.response.tvShows


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class NetworkTvShowsListItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)