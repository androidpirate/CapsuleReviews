package com.github.androidpirate.capsulereviews.data.network.response.tvShow.external_ids


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class TvShowExternalIDs(
    @Nullable
    @SerializedName("id")
    val id: Int,
    @Nullable
    @SerializedName("imdb_id")
    val imdbId: String,
    @Nullable
    @SerializedName("tvdb_id")
    val tvdbId: Int
)