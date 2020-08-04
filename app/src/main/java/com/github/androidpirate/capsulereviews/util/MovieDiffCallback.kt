package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.Movie

class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        TODO("Not yet implemented")
    }
}