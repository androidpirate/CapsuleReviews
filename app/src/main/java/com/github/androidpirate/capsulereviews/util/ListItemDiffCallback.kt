package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.data.network.response.tvShow.NetworkTvShow
import com.github.androidpirate.capsulereviews.data.network.response.tvShows.NetworkTvShowsListItem
import java.lang.IllegalArgumentException

class ListItemDiffCallback<T>(val fragmentType: String?): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as DBMovie).id == (newItem as DBMovie).id
            MOVIE_DETAIL -> (oldItem as NetworkMoviesListItem).id == (newItem as NetworkMoviesListItem).id
            TV_LIST -> (oldItem as NetworkTvShowsListItem).id == (newItem as NetworkTvShowsListItem).id
            TV_DETAIL -> (oldItem as NetworkTvShowsListItem).id == (newItem as NetworkTvShowsListItem).id
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as DBMovie).title == (newItem as DBMovie).title
            MOVIE_DETAIL -> (oldItem as NetworkMoviesListItem).title == (newItem as NetworkMoviesListItem).title
            TV_LIST -> (oldItem as NetworkTvShowsListItem).name == (newItem as NetworkTvShowsListItem).name
            TV_DETAIL -> (oldItem as NetworkTvShowsListItem).name == (newItem as NetworkTvShowsListItem).name
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    companion object {
        const val MOVIE_LIST = "MovieListFragment"
        const val TV_LIST = "TvListFragment"
        const val MOVIE_DETAIL = "MovieDetailFragment"
        const val TV_DETAIL = "TvDetailFragment"
    }
}