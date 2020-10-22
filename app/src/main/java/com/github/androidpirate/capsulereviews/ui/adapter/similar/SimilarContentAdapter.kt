package com.github.androidpirate.capsulereviews.ui.adapter.similar

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
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import kotlinx.android.synthetic.main.similar_list_item.view.*
import java.lang.IllegalArgumentException

class SimilarContentAdapter<T>(
    private val fragment: FragmentType,
    private val clickListener: SimilarContentClickListener):
    ListAdapter<T, SimilarContentAdapter<T>.SimilarContentHolder>(SimilarContentDiffCallback<T>(fragment)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarContentHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.similar_list_item, parent, false)
        return SimilarContentHolder(itemView)
    }

    override fun onBindViewHolder(holder: SimilarContentHolder, position: Int) {
        getItem(position)?.let { holder.onBindSimilar(it) }
    }

    inner class SimilarContentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun onBindSimilar(item: T) {
            itemView.setOnClickListener {
                clickListener.onItemClick(item)
            }
            when(fragment) {
                MOVIE_DETAIL ->
                    setSimilarItemThumbnail((item as NetworkMoviesListItem).posterPath)
                TV_DETAIL ->
                    setSimilarItemThumbnail((item as NetworkTvShowsListItem).posterPath)
                else ->
                    throw IllegalArgumentException("${Constants.ADAPTER_UNKNOWN_FRAGMENT_MESSAGE}: $fragment")
            }
        }

        private fun setSimilarItemThumbnail(posterPath: String) {
            Glide.with(itemView.context)
                .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + Constants.ADAPTER_POSTER_WIDTH + posterPath)
                .placeholder(R.drawable.ic_no_preview)
                .into(itemView.ivSimilarListItem)
        }
    }
}