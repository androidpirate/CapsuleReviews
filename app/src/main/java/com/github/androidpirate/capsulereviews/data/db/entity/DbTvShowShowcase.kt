package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvShowcase")
data class DbTvShowShowcase(
    val tvShowId: Int,
    val title: String,
    val posterPath: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = ID
    var videoKey: String = EMPTY_VIDEO_KEY

    companion object {
        const val ID = 0
        const val EMPTY_VIDEO_KEY = ""
    }
}