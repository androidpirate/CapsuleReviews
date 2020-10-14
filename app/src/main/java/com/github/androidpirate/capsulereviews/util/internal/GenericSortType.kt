package com.github.androidpirate.capsulereviews.util.internal

enum class GenericSortType {
    POPULAR,
    TOP_RATED,
    UPCOMING,
    NOW_PLAYING,
    TRENDING;

    companion object {
        fun getAllTypesArray(): Array<GenericSortType> {
            return arrayOf(POPULAR, TOP_RATED, NOW_PLAYING, UPCOMING, TRENDING)
        }
    }
}