package com.github.androidpirate.capsulereviews.ui.tv.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.TvShow
import com.github.androidpirate.capsulereviews.util.TvShowCallback
import java.lang.IllegalArgumentException

class TvListAdapter: ListAdapter<TvShow, TvListAdapter.TvShowHolder>(TvShowCallback()) {

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
        holder.onBindTvShow(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> R.layout.see_more_list_item
            else -> R.layout.list_item
        }
    }

    class TvShowHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBindTvShow(show: TvShow) {

        }
    }
}