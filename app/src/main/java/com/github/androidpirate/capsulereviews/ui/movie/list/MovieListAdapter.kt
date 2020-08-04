package com.github.androidpirate.capsulereviews.ui.movie.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.Movie
import com.github.androidpirate.capsulereviews.util.MovieDiffCallback
import java.lang.IllegalArgumentException

class MovieListAdapter: ListAdapter<Movie, MovieListAdapter.MovieHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return when(viewType) {
            R.layout.see_more_list_item -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.see_more_list_item, parent, false)
                MovieHolder(itemView)
            }
            R.layout.list_item -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                MovieHolder(itemView)
            }
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.onBindMovie(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> R.layout.see_more_list_item
            else -> R.layout.list_item
        }
    }

    class MovieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBindMovie(movie: Movie) {

        }
    }
}