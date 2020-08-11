package com.github.androidpirate.capsulereviews.data.network.response.movies


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class MoviesListItem(
    @Nullable
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("popularity")
    val popularity: Double,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double
)