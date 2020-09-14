package com.github.androidpirate.capsulereviews.util.internal

import com.github.androidpirate.capsulereviews.BuildConfig

enum class SortType(val queryParameter: String) {
    POPULAR_DESCENDING(BuildConfig.POPULARITY_DESC),
    PRIMARY_RELEASE_DATE_DESCENDING(BuildConfig.RELEASE_DATE_DESC),
    VOTE_AVERAGE_DESCENDING(BuildConfig.VOTE_AVERAGE_DESC)
}