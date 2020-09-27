package com.github.androidpirate.capsulereviews.data.network.response.tvShow


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.github.androidpirate.capsulereviews.util.ContentFormatter
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.google.gson.annotations.SerializedName

data class NetworkTvShow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("created_by")
    val createdBy: List<NetworkCreatedBy>,
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
    val networkGenres: List<NetworkGenre>,
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
) {
    fun toFavorite() = DBFavorite(
        id = this.id,
        title = this.name,
        overview = this.overview,
        releaseDate = ContentFormatter.formatReleaseDate(this.releaseDate),
        genres = ContentFormatter.formatGenres(this.networkGenres),
        voteAverage = this.voteAverage,
        type = Constants.FAVORITE_TV_SHOW_TYPE,
        bingeStatus = Constants.DEFAULT_BINGE_STATUS,
        runtime = Constants.EMPTY_FIELD_STRING,
        createdBy = ContentFormatter.formatCreatedBy(this.createdBy),
        episodeRunTime = ContentFormatter.formatTvShowRunTime(episodeRunTime[0]),
        status = this.status,
        numberOfSeasons = this.numberOfSeasons,
        numberOfEpisodes = this.numberOfEpisodes,
        networks = ContentFormatter.formatNetworks(this.networkNetworkInfos)
    )
}