package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class DBFavorite(
    // Shared fields
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val genres: String,
    val voteAverage: Double,
    val type: String,
    val bingeStatus: String,
    // Movie details
    val runtime: String,
    // Tv Show details
    val createdBy: String,
    val episodeRunTime: String,
    val status: String,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val networks: String
)