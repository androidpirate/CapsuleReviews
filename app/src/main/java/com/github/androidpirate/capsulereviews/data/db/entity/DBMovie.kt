package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class DBMovie(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val posterPath: String,
    val video: Boolean,
    val voteAverage: Double,
    val popular: Boolean = false,
    val topRated: Boolean = false,
    val nowPlaying:Boolean = false,
    val upcoming:Boolean = false,
    val trending: Boolean = false
)