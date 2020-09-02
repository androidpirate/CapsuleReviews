package com.github.androidpirate.capsulereviews.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.util.internal.SortType
import kotlinx.android.synthetic.main.list_item.view.*

class PagedMoviesAdapter(private val clickListener: ItemClickListener)
    : PagedListAdapter<NetworkMoviesListItem, PagedMoviesAdapter.ItemHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        getItem(position)?.let { holder.onBindItem(it) }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(item: NetworkMoviesListItem) {
            itemView.setOnClickListener {
                clickListener.onItemClick(item, false, SortType.POPULAR)
            }
            setItemThumbnail(item.posterPath)
        }

        private fun setItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                // TODO 4: Take care of the base URL when saving data into local db
                .load("https://image.tmdb.org/t/p/w185/" + posterPath)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(itemView.ivListItem)
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<NetworkMoviesListItem>() {
        override fun areItemsTheSame(oldItem: NetworkMoviesListItem, newItem: NetworkMoviesListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NetworkMoviesListItem, newItem: NetworkMoviesListItem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}