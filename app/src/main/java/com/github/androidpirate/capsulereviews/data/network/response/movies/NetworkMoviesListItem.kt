package com.github.androidpirate.capsulereviews.data.network.response.movies


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
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
) {
    fun toPopularMovie() = DBMovie(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video,
        this.voteAverage,
        popular = true
    )

    fun toTopRated() = DBMovie(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video,
        this.voteAverage,
        topRated = true
    )

    fun toNowPlaying() = DBMovie(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video,
        this.voteAverage,
        nowPlaying = true
    )

    fun toUpcoming() = DBMovie(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video,
        this.voteAverage,
        upcoming = true
    )

    fun toTrending() = DBMovie(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video,
        this.voteAverage,
        trending = true
    )

    fun toShowcase() = DBMovieShowcase(
        this.id,
        this.title,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.video
    )

    companion object {
        const val EMPTY_POSTER_PATH = ""
    }
}