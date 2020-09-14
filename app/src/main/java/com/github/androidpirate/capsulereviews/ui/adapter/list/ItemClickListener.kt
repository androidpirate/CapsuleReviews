package com.github.androidpirate.capsulereviews.ui.adapter.list

import com.github.androidpirate.capsulereviews.util.internal.GenericSortType

interface ItemClickListener {

    fun<T> onItemClick(item: T, isLast: Boolean, genericSort: GenericSortType)
}