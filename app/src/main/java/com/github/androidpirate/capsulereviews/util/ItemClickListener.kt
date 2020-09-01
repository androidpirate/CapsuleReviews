package com.github.androidpirate.capsulereviews.util

interface ItemClickListener {

    fun<T> onItemClick(item: T, isLast: Boolean)
}