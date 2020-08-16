package com.github.androidpirate.capsulereviews.data.domain

import com.github.androidpirate.capsulereviews.BuildConfig

data class MoviesListItem(
    val _id: Int,
    val _posterPath: String?
) {
    private val id = _id
    private val posterPath = _posterPath
        get() {
            return if(field == null) {
                EMPTY
            } else {
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL + field
            }
        }

    companion object {
        const val EMPTY = ""
    }
}