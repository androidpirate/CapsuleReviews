package com.github.androidpirate.capsulereviews.ui.adapter.paged

interface PagedItemClickListener {
    fun <T : Any> onPagedItemClick(item: T)
}