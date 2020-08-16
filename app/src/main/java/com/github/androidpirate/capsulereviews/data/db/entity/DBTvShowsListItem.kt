package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_shows")
data class DBTvShowsListItem(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)