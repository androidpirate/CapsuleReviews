package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "now_playing_movies")
data class DBNowPlayingMovie(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)