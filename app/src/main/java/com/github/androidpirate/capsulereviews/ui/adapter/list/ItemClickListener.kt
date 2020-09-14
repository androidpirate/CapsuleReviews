package com.github.androidpirate.capsulereviews.ui.adapter.list

import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType

interface ItemClickListener {

    fun<T> onItemClick(
        item: T, 
        isLast: Boolean, 
        genericSort: GenericSortType, 
        network: NetworkType, 
        genre: GenreType
    )
}