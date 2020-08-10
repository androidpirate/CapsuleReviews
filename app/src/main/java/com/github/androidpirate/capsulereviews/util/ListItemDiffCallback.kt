package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.response.movie.MovieResponse
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.data.response.tvShow.TvShowResponse
import com.github.androidpirate.capsulereviews.data.response.tvShows.TvShowsListItem
import java.lang.IllegalArgumentException

class ListItemDiffCallback<T>(val fragmentType: String?): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as MoviesListItem).id == (newItem as MoviesListItem).id
            MOVIE_DETAIL -> (oldItem as MovieResponse).id == (newItem as MovieResponse).id
            TV_LIST -> (oldItem as TvShowsListItem).id == (newItem as TvShowsListItem).id
            TV_DETAIL -> (oldItem as TvShowResponse).id == (newItem as TvShowResponse).id
            else -> throw IllegalArgumentException("Unknown fragment type: $fragmentType")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragmentType) {
            MOVIE_LIST -> (oldItem as MoviesListItem).title == (newItem as MoviesListItem).title
            MOVIE_DETAIL -> (oldItem as MovieResponse).title == (newItem as MovieResponse).title
            TV_LIST -> (oldItem as TvShowsListItem).name == (newItem as TvShowsListItem).name
            TV_DETAIL -> (oldItem as TvShowResponse).name == (newItem as TvShowResponse).name
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