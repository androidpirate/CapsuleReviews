package com.github.androidpirate.capsulereviews.data.network.response.tvShows


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class TvShowsListItem(
    @Nullable
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Double,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)