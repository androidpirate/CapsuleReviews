package com.github.androidpirate.capsulereviews.util

import com.github.androidpirate.capsulereviews.util.internal.SortType

interface ItemClickListener {

    fun<T> onItemClick(item: T, isLast: Boolean, sort: SortType)
}