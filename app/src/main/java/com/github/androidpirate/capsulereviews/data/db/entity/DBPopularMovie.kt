package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movies")
data class DBPopularMovie(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)