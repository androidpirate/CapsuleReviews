package com.github.androidpirate.capsulereviews.ui.adapter.dialog

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.androidpirate.capsulereviews.R
import kotlinx.android.synthetic.main.genres_list_item.view.*

class GenreAdapter(
    private val genres: Array<String>,
    private val listener: GenreClickListener,
    selectedGenre: Int): RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    private var index = selectedGenre

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.genres_list_item, parent, false)
        return GenreHolder(view)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.onBindGenre(genres[position])
        holder.itemView.genreTitle.setOnClickListener {
            listener.onGenreClick(position)
        }
        val genreTitle = holder.itemView.genreTitle
        if(index == position) {
            genreTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
            genreTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        } else {
            genreTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorWhite))
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    class GenreHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBindGenre(genre: String) {
            itemView.genreTitle.text = genre
        }
    }
}