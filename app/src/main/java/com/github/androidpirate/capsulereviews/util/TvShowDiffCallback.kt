package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem

class TvShowDiffCallback(): DiffUtil.ItemCallback<TvShowsListItem>() {
    override fun areItemsTheSame(oldItem: TvShowsListItem, newItem: TvShowsListItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: TvShowsListItem, newItem: TvShowsListItem): Boolean {
        TODO("Not yet implemented")
    }
}