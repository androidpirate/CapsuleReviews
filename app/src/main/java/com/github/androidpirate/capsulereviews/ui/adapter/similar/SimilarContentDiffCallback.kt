package com.github.androidpirate.capsulereviews.ui.adapter.similar

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import java.lang.IllegalArgumentException

class SimilarContentDiffCallback<T>(
    private val fragment: FragmentType): DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_DETAIL ->
                (oldItem as NetworkMoviesListItem).id == (newItem as NetworkMoviesListItem).id
            TV_DETAIL ->
                (oldItem as NetworkTvShowsListItem).id == (newItem as NetworkTvShowsListItem).id
            else ->
                throw IllegalArgumentException("${Constants.ILLEGAL_FRAGMENT_TYPE_EXCEPTION} $fragment")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_DETAIL ->
                (oldItem as NetworkMoviesListItem).title == (newItem as NetworkMoviesListItem).title
            TV_DETAIL ->
                (oldItem as NetworkTvShowsListItem).name == (newItem as NetworkTvShowsListItem).name
            else ->
                throw IllegalArgumentException("${Constants.ILLEGAL_FRAGMENT_TYPE_EXCEPTION} $fragment")
        }
    }
}