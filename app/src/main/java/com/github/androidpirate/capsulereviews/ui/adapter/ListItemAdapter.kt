package com.github.androidpirate.capsulereviews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
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
                    holder.onBindItem(getItem(position))
                }
            }
            MOVIE_DETAIL -> holder.onBindItem(getItem(position))
            TV_LIST -> {
                if(position != itemCount - 1) {
                    holder.onBindItem(getItem(position))
                }
            }
            TV_DETAIL -> holder.onBindItem(getItem(position))
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

    override fun getItemCount(): Int {
        return if(fragmentType == MOVIE_LIST && currentList.size > ITEM_COUNT_LIMIT) {
            ITEM_COUNT_LIMIT
        } else if(fragmentType == MOVIE_LIST && currentList.size <= ITEM_COUNT_LIMIT) {
            currentList.size
        }else if(fragmentType == MOVIE_DETAIL) {
            currentList.size
        }else if(fragmentType == TV_LIST && currentList.size > ITEM_COUNT_LIMIT) {
            ITEM_COUNT_LIMIT
        } else if(fragmentType == TV_LIST && currentList.size <= ITEM_COUNT_LIMIT) {
            currentList.size
        }else if(fragmentType == TV_DETAIL) {
            currentList.size
        } else {
            throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    inner class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(item: T) {
            itemView.setOnClickListener { clickListener.onItemClick(item) }
            when(fragmentType) {
                MOVIE_LIST -> setItemThumbnail((item as DBMovie).posterPath)
                MOVIE_DETAIL -> setSimilarItemThumbnail((item as NetworkMoviesListItem).posterPath)
                TV_LIST -> setItemThumbnail((item as NetworkTvShowsListItem).posterPath)
                TV_DETAIL -> setSimilarItemThumbnail((item as NetworkTvShowsListItem).posterPath)
                else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
            }
        }

        private fun setItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
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
        const val ITEM_COUNT_LIMIT = 12
        const val MOVIE_LIST = "MovieListFragment"
        const val TV_LIST = "TvListFragment"
        const val MOVIE_DETAIL = "MovieDetailFragment"
        const val TV_DETAIL = "TvDetailFragment"
    }
}