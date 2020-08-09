package com.github.androidpirate.capsulereviews.util

import androidx.recyclerview.widget.DiffUtil

class ListItemDiffCallback<T>(): DiffUtil.ItemCallback<T> () {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        TODO("Not yet implemented")
    }
}