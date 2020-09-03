package com.github.androidpirate.capsulereviews.ui.adapter.list

import androidx.recyclerview.widget.DiffUtil
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBTvShow
import com.github.androidpirate.capsulereviews.util.internal.FragmentType
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import java.lang.IllegalArgumentException

class ListItemDiffCallback<T>(private val fragment: FragmentType): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_LIST -> (oldItem as DBMovie).id == (newItem as DBMovie).id
            TV_LIST -> (oldItem as DBTvShow).id == (newItem as DBTvShow).id
            else -> throw IllegalArgumentException("$ARGUMENT_EXCEPTION_MESSAGE $fragment")
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when(fragment) {
            MOVIE_LIST -> (oldItem as DBMovie).title == (newItem as DBMovie).title
            TV_LIST -> (oldItem as DBTvShow).title == (newItem as DBTvShow).title
            else -> throw IllegalArgumentException("$ARGUMENT_EXCEPTION_MESSAGE $fragment")
        }
    }

    companion object {
        const val ARGUMENT_EXCEPTION_MESSAGE = "Unknown fragment type:"
    }
}