package com.github.androidpirate.capsulereviews.ui.adapter.pager

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.db.entity.DBFavorite

class PagerItemDiffCallback: DiffUtil.ItemCallback<DBFavorite>() {
    override fun areItemsTheSame(oldItem: DBFavorite, newItem: DBFavorite): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DBFavorite, newItem: DBFavorite): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.title == newItem.title &&
                oldItem.bingeStatus == newItem.bingeStatus
    }
}