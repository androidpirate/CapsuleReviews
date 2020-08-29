package com.github.androidpirate.capsulereviews.data.network.response.tvShows


import androidx.annotation.Nullable
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.db.entity.DbTvShowShowcase
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
) {
    fun toPopular() = DBTvShow(
        this.id,
        this.name,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.voteAverage,
        popular = true
    )

    fun toTopRated() = DBTvShow(
        this.id,
        this.name,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.voteAverage,
        topRated = true
    )

    fun toTrending() = DBTvShow(
        this.id,
        this.name,
        this.posterPath ?: EMPTY_POSTER_PATH,
        this.voteAverage,
        trending = true
    )

    fun toShowcase() = DbTvShowShowcase(
        this.id,
        this.name,
        this.posterPath ?: EMPTY_POSTER_PATH
    )

    companion object {
        const val EMPTY_POSTER_PATH = ""
    }
}