package com.github.androidpirate.capsulereviews.data.network.response.movie


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.google.gson.annotations.SerializedName

data class NetworkMovie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @Nullable
    @SerializedName("tagline")
    val tagLine: String,
    @Nullable
    @SerializedName("overview")
    val overview: String,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("genres")
    val networkGenres: List<NetworkGenre>,
    @SerializedName("runtime")
    @Nullable
    val runtime: Int,
    @SerializedName("budget")
    val budget: Long,
    @SerializedName("revenue")
    val revenue: Long,
    @Nullable
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double
)