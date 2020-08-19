package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upcoming_movies")
data class DBUpcomingMovie(
    @PrimaryKey
    val id: Int,
    val posterPath: String
)