package com.github.androidpirate.capsulereviews.ui.movie.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.MovieDiffCallback
import java.lang.IllegalArgumentException
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MovieListAdapter(private val clickListener: ItemClickListener):
    ListAdapter<MoviesListItem, MovieListAdapter.MovieHolder>(MovieDiffCallback()) {

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
        if(position != itemCount - 1) {
            holder.onBindMovie(getItem(position), clickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> R.layout.see_more_list_item
            else -> R.layout.list_item
        }
    }

    class MovieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBindMovie(movie: MoviesListItem, clickListener: ItemClickListener) {
            setMovieThumbnail(movie.posterPath)
            itemView.setOnClickListener { clickListener.onItemClick(movie) }
        }

        private fun setMovieThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
                .into(itemView.ivListItem)
        }
    }
}