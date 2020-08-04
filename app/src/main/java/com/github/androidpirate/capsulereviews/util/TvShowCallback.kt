package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.TvShow

class TvShowCallback(): DiffUtil.ItemCallback<TvShow>() {
    override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
        TODO("Not yet implemented")
    }
}