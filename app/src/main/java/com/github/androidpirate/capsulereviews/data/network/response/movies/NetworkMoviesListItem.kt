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
    fun toPopular() = DBMovie(
        this.id,
        this.title,
        this.posterPath,
        this.video,
        this.voteAverage,
        popular = true
    )
}