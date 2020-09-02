package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import java.lang.IllegalArgumentException

class ListItemDiffCallback<T>(private val fragmentType: FragmentType): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as DBMovie).id == (newItem as DBMovie).id
            MOVIE_DETAIL -> (oldItem as NetworkMoviesListItem).id == (newItem as NetworkMoviesListItem).id
            TV_LIST -> (oldItem as DBTvShow).id == (newItem as DBTvShow).id
            TV_DETAIL -> (oldItem as NetworkTvShowsListItem).id == (newItem as NetworkTvShowsListItem).id
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as DBMovie).title == (newItem as DBMovie).title
            MOVIE_DETAIL -> (oldItem as NetworkMoviesListItem).title == (newItem as NetworkMoviesListItem).title
            TV_LIST -> (oldItem as DBTvShow).title == (newItem as DBTvShow).title
            TV_DETAIL -> (oldItem as NetworkTvShowsListItem).name == (newItem as NetworkTvShowsListItem).name
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }
}