package com.github.androidpirate.capsulereviews.data.network.response.tvShow


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.google.gson.annotations.SerializedName

data class NetworkTvShow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("created_by")
    val networkCreatedBy: List<NetworkCreatedBy>,
    @SerializedName("first_air_date")
    val releaseDate: String,
    @SerializedName("name")
    val name: String,
    @Nullable
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>,
    @SerializedName("genres")
    val genres: List<NetworkGenre>,
    @SerializedName("status")
    val status: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("networks")
    val networkNetworkInfos: List<NetworkNetworkInfo>,
    @SerializedName("type")
    val type: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)