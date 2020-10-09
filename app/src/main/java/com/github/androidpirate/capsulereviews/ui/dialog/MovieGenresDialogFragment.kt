package com.github.androidpirate.capsulereviews.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.MovieGenresAdapter
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import kotlinx.android.synthetic.main.movie_genres_dialog.*

class MovieGenresDialogFragment(
    private val listener: MovieGenresDialogListener,
    private val selectedPosition: Int) :
    DialogFragment(),
    MovieGenresAdapter.GenreClickListener {

    private lateinit var adapter: MovieGenresAdapter

    interface MovieGenresDialogListener {
        fun onGenreSelected(genre: GenreType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MovieGenresAdapter(this, selectedPosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_genres_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
        movieGenres.layoutManager = LinearLayoutManager(requireContext())
        movieGenres.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun getSelectedGenre(itemPosition: Int): GenreType {
        val map = Constants.getMovieGenresMap()
        return map[Constants.getMovieGenresArray()[itemPosition]] ?: error("No such genre in the array!")
    }

    override fun onGenreClick(itemPosition: Int) {
        listener.onGenreSelected(getSelectedGenre(itemPosition))
        dialog?.cancel()
    }

    companion object {
        fun newInstance(listener: MovieGenresDialogListener, selectedPosition: Int): MovieGenresDialogFragment {
            return MovieGenresDialogFragment(listener, selectedPosition)
        }
    }
}