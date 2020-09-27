package com.github.androidpirate.capsulereviews.ui.adapter.pager

interface PagerListItemClickListener {
    fun onPagerItemClick(itemId: Int)
    fun onUnFavoriteClick(itemId: Int)
    fun onChangeStatusClick(itemId: Int)
}