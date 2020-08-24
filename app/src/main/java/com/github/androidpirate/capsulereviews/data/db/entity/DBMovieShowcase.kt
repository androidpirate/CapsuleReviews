package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_showcase")
data class DBMovieShowcase(
    val movieId: Int,
    val title: String,
    val posterPath: String,
    val video: Boolean
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = ID
    var videoKey: String = EMPTY_VIDEO_KEY

    companion object {
        const val ID = 0
        const val EMPTY_VIDEO_KEY = ""
    }
}