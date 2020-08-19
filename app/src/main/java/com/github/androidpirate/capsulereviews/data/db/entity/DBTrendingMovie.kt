package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_movies")
data class DBTrendingMovie(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)