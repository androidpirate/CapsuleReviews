package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_rated_movies")
data class DBTopRatedMovie(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)