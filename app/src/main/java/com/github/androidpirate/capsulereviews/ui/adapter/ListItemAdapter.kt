package com.github.androidpirate.capsulereviews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.ListItemDiffCallback
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.similar_list_item.view.*
import java.lang.IllegalArgumentException

class ListItemAdapter<T>(private val fragmentType: String?, private val clickListener: ItemClickListener)
    : ListAdapter<T, ListItemAdapter<T>.ListItemHolder>(ListItemDiffCallback<T>(fragmentType)){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.list_item ->
                ListItemHolder(layoutInflater.inflate(
                    R.layout.list_item, parent, false))
            R.layout.see_more_list_item ->
                ListItemHolder(layoutInflater.inflate(
                    R.layout.see_more_list_item, parent, false))
            else ->
                ListItemHolder(layoutInflater.inflate(
                    R.layout.similar_list_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        when(fragmentType) {
            MOVIE_LIST -> {
                if(position != itemCount - 1) {
                    holder.onBindItem(getItem(position), false)
                } else if(position == itemCount -1) {
                    holder.onBindItem(getItem(position), true)
                }
            }
            MOVIE_DETAIL -> holder.onBindItem(getItem(position), false)
            TV_LIST -> {
                if(position != itemCount - 1) {
                    holder.onBindItem(getItem(position), false)
                } else if(position == itemCount -1) {
                    holder.onBindItem(getItem(position), true)
                }
            }
            TV_DETAIL -> holder.onBindItem(getItem(position), false)
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(position) {
            itemCount - 1 -> {
                return when (fragmentType) {
                    MOVIE_LIST -> R.layout.see_more_list_item
                    MOVIE_DETAIL -> R.layout.similar_list_item
                    TV_LIST -> R.layout.see_more_list_item
                    TV_DETAIL -> R.layout.similar_list_item
                    else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
                }
            }
            else -> {
                return when(fragmentType) {
                    MOVIE_LIST -> R.layout.list_item
                    MOVIE_DETAIL -> R.layout.similar_list_item
                    TV_LIST -> R.layout.list_item
                    TV_DETAIL -> R.layout.similar_list_item
                    else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
                }
            }
        }
    }

    inner class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(item: T, lastItem: Boolean) {
            itemView.setOnClickListener {
                clickListener.onItemClick(item, lastItem)
            }
            when(fragmentType) {
                MOVIE_LIST -> if(!lastItem) { setItemThumbnail((item as DBMovie).posterPath) }
                MOVIE_DETAIL -> setSimilarItemThumbnail((item as NetworkMoviesListItem).posterPath)
                TV_LIST -> if(!lastItem) { setItemThumbnail((item as DBTvShow).posterPath) }
                TV_DETAIL -> setSimilarItemThumbnail((item as NetworkTvShowsListItem).posterPath)
                else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
            }
        }

        private fun setItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(itemView.ivListItem)
        }

        private fun setSimilarItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
                .into(itemView.ivSimilarListItem)
        }
    }

    companion object {
        const val MOVIE_LIST = "MovieListFragment"
        const val TV_LIST = "TvListFragment"
        const val MOVIE_DETAIL = "MovieDetailFragment"
        const val TV_DETAIL = "TvDetailFragment"
    }
}