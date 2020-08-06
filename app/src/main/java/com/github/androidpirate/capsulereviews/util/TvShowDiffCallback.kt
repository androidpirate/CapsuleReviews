package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsResult

class TvShowDiffCallback(): DiffUtil.ItemCallback<TvShowsResult>() {
    override fun areItemsTheSame(oldItem: TvShowsResult, newItem: TvShowsResult): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: TvShowsResult, newItem: TvShowsResult): Boolean {
        TODO("Not yet implemented")
    }
}