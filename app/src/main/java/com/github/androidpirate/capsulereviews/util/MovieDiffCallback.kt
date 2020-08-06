package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesResult

class MovieDiffCallback: DiffUtil.ItemCallback<MoviesResult>() {
    override fun areItemsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: MoviesResult, newItem: MoviesResult): Boolean {
        TODO("Not yet implemented")
    }
}