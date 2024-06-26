package com.github.androidpirate.capsulereviews.ui.adapter.list

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import java.lang.IllegalArgumentException

class ListItemDiffCallback<T>(
    private val fragment: FragmentType): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_LIST ->
                (oldItem as NetworkMoviesListItem).id == (newItem as NetworkMoviesListItem).id
            TV_LIST ->
                (oldItem as NetworkTvShowsListItem).id == (newItem as NetworkTvShowsListItem).id
            else ->
                throw IllegalArgumentException("${Constants.ILLEGAL_FRAGMENT_TYPE_EXCEPTION} $fragment")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_LIST ->
                (oldItem as NetworkMoviesListItem).title == (newItem as NetworkMoviesListItem).title
            TV_LIST ->
                (oldItem as NetworkTvShowsListItem).name == (newItem as NetworkTvShowsListItem).name
            else ->
                throw IllegalArgumentException("${Constants.ILLEGAL_FRAGMENT_TYPE_EXCEPTION} $fragment")
        }
    }
}