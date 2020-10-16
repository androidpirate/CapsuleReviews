package com.github.androidpirate.capsulereviews.ui.adapter.paged

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.IllegalArgumentException

class PagedItemAdapter<T>(private val fragment: FragmentType, val clickListener: PagedItemClickListener)
    : PagedListAdapter<T, PagedItemAdapter<T>.PagedItemHolder>(PagedItemDiffCallback<T>(fragment)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedItemHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return PagedItemHolder(itemView)
    }

    override fun onBindViewHolder(holderPaged: PagedItemHolder, position: Int) {
        getItem(position)?.let { holderPaged.onBindItem(it) }
    }

    inner class PagedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBindItem(item: T) {
            itemView.setOnClickListener {
                clickListener.onPagedItemClick(item)
            }
            when(fragment) {
                PAGED_MOVIE_LIST -> setItemThumbnail((item as NetworkMoviesListItem).posterPath)
                PAGED_TV_LIST -> setItemThumbnail((item as NetworkTvShowsListItem).posterPath)
                else -> throw IllegalArgumentException("${Constants.ADAPTER_UNKNOWN_FRAGMENT_MESSAGE} $fragment")
            }
        }

        private fun setItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + Constants.ADAPTER_POSTER_WIDTH+ posterPath)
                .placeholder(R.drawable.ic_no_preview)
                .into(itemView.ivListItem)
        }

    }
}