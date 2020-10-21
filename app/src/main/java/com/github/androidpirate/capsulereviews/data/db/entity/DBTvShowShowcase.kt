package com.github.androidpirate.capsulereviews.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.androidpirate.capsulereviews.util.internal.Constants

@Entity(tableName = "tvShowcase")
data class DBTvShowShowcase(
    val tvShowId: Int,
    val title: String,
    val posterPath: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = Constants.SHOWCASE_ID
    var videoKey: String = Constants.EMPTY_VIDEO_KEY
}