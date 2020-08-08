package com.github.androidpirate.capsulereviews.ui.movie.detail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.MovieDiffCallback

class SimilarMoviesAdapter(private val clickListener: ItemClickListener)
    : ListAdapter<MoviesListItem, SimilarMoviesAdapter.SimilarMovieHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SimilarMovieHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class SimilarMovieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

}