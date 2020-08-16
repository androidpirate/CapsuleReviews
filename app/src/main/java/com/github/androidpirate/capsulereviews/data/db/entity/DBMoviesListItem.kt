package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class DBMoviesListItem(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)