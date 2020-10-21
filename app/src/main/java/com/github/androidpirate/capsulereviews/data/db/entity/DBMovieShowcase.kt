package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.androidpirate.capsulereviews.util.internal.Constants

@Entity(tableName = "movieShowcase")
data class DBMovieShowcase(
    val movieId: Int,
    val title: String,
    val posterPath: String,
    val video: Boolean
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = Constants.SHOWCASE_ID
    var videoKey: String = Constants.EMPTY_VIDEO_KEY
}