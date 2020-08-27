package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_shows")
data class DBTvShow(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
    val popular: Boolean = false,
    val topRated: Boolean = false,
    val trending: Boolean = false
)