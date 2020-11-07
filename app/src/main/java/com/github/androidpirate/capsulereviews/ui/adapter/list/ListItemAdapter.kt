package com.github.androidpirate.capsulereviews.ui.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.*
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.IllegalArgumentException

class ListItemAdapter<T> (
    private val fragment: FragmentType,
    private val genericSort: GenericSortType,
    private val network: NetworkType,
    private val genre: GenreType,
    private val clickListener: ItemClickListener
) : ListAdapter<T, ListItemAdapter<T>.ListItemHolder>(ListItemDiffCallback<T>(fragment)){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.list_item ->
                ListItemHolder(layoutInflater.inflate(
                    R.layout.list_item, parent, false))
            R.layout.see_more_list_item ->
                ListItemHolder(layoutInflater.inflate(
                    R.layout.see_more_list_item, parent, false))
            else -> throw IllegalArgumentException("${Constants.ADAPTER_UNKNOWN_VIEW_TYPE_MESSAGE} $viewType")
        }
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        when(fragment) {
            MOVIE_LIST -> {
                if(position != itemCount - 1) {
                    getItem(position)?.let { holder.onBindItem(it, false) }
                } else if(position == itemCount - 1) {
                    getItem(position)?.let { holder.onBindItem(it, true) }
                }
            }
            TV_LIST -> {
                if(position != itemCount - 1) {
                    getItem(position)?.let { holder.onBindItem(it, false) }
                } else if(position == itemCount -1) {
                    getItem(position)?.let { holder.onBindItem(it, true) }
                }
            }
            else -> throw IllegalArgumentException("${Constants.ADAPTER_UNKNOWN_FRAGMENT_MESSAGE} $fragment")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> R.layout.see_more_list_item
            else -> R.layout.list_item
        }
    }

    inner class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(item: T, lastItem: Boolean) {
            itemView.setOnClickListener {
                clickListener.onItemClick(item, lastItem, genericSort, network, genre)
            }
            when(fragment) {
                MOVIE_LIST -> if(!lastItem) { setItemThumbnail((item as NetworkMoviesListItem).posterPath) }
                TV_LIST -> if(!lastItem) { setItemThumbnail((item as NetworkTvShowsListItem).posterPath) }
                else -> throw IllegalArgumentException("${Constants.ADAPTER_UNKNOWN_FRAGMENT_MESSAGE}: $fragment")
            }
        }

        private fun setItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + Constants.ADAPTER_POSTER_WIDTH + posterPath)
                .placeholder(R.drawable.ic_no_preview)
                .into(itemView.ivListItem)
        }
    }
}