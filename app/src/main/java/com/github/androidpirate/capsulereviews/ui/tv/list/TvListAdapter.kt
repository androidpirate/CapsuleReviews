package com.github.androidpirate.capsulereviews.ui.tv.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.TvShowDiffCallback
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.IllegalArgumentException

class TvListAdapter(private val clickListener: ItemClickListener):
    ListAdapter<TvShowsListItem, TvListAdapter.TvShowHolder>(TvShowDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowHolder {
        return when(viewType) {
            R.layout.list_item -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                TvShowHolder(itemView)
            }
            R.layout.see_more_list_item -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.see_more_list_item, parent, false)
                TvShowHolder(itemView)
            }
            else -> throw (IllegalArgumentException("Unknown view type: $viewType"))
        }
    }

    override fun onBindViewHolder(holder: TvShowHolder, position: Int) {
        if(position != itemCount - 1 ) {
            holder.onBindTvShow(getItem(position), clickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> R.layout.see_more_list_item
            else -> R.layout.list_item
        }
    }

    override fun getItemCount(): Int {
        if(currentList.size > ITEM_COUNT_LIMIT) {
            return ITEM_COUNT_LIMIT
        }
        return currentList.size
    }

    class TvShowHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBindTvShow(show: TvShowsListItem, clickListener: ItemClickListener) {
            setTvShowThumbnail(show.posterPath)
            itemView.setOnClickListener { clickListener.onItemClick(show) }
        }

        private fun setTvShowThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
                .into(itemView.ivListItem)
        }
    }

    companion object {
        const val ITEM_COUNT_LIMIT = 20
    }
}