package com.github.androidpirate.capsulereviews.data.response.tvShow.external_ids


import com.google.gson.annotations.SerializedName

data class TvShowExternalIDs(
    @SerializedName("facebook_id")
    val facebookId: String,
    @SerializedName("freebase_id")
    val freebaseId: String,
    @SerializedName("freebase_mid")
    val freebaseMid: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("instagram_id")
    val instagramId: String,
    @SerializedName("tvdb_id")
    val tvdbId: Int,
    @SerializedName("tvrage_id")
    val tvrageId: Int,
    @SerializedName("twitter_id")
    val twitterId: String
)