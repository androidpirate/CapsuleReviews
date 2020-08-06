package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem

class MovieDiffCallback: DiffUtil.ItemCallback<MoviesListItem>() {
    override fun areItemsTheSame(oldItem: MoviesListItem, newItem: MoviesListItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: MoviesListItem, newItem: MoviesListItem): Boolean {
        TODO("Not yet implemented")
    }
}