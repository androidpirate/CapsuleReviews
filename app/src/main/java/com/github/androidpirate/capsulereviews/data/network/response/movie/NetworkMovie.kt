package com.github.androidpirate.capsulereviews.data.network.response.movie


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite
import com.github.androidpirate.capsulereviews.data.network.response.genre.NetworkGenre
import com.github.androidpirate.capsulereviews.util.ContentFormatter
import com.github.androidpirate.capsulereviews.util.internal.Constants
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
) {
    fun toFavorite() = DBFavorite(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = ContentFormatter.formatReleaseDate(this.releaseDate),
        genres = ContentFormatter.formatGenres(this.networkGenres),
        voteAverage = this.voteAverage,
        type = Constants.FAVORITE_MOVIE_TYPE,
        bingeStatus = Constants.DEFAULT_BINGE_STATUS,
        runtime = ContentFormatter.formatMovieRunTime(this.runtime),
        createdBy = Constants.EMPTY_FIELD_STRING,
        episodeRunTime = Constants.EMPTY_FIELD_STRING,
        status = Constants.EMPTY_FIELD_STRING,
        numberOfEpisodes = Constants.EMPTY_FIELD_INT,
        numberOfSeasons = Constants.EMPTY_FIELD_INT,
        networks = Constants.EMPTY_FIELD_STRING
    )
}