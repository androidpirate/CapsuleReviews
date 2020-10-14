package com.github.androidpirate.capsulereviews.ui.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.GenreAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.GenreClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import kotlinx.android.synthetic.main.movie_genres_dialog.*

class MovieGenresDialogFragment(
    private val listener: MovieGenresDialogListener,
    private val selectedPosition: Int):
    DialogFragment(),
    GenreClickListener {

    private lateinit var adapter: GenreAdapter

    interface MovieGenresDialogListener {
        fun onGenreSelected(genre: GenreType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GenreAdapter(Constants.getMovieGenresArray(),this, selectedPosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_genres_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
        movieGenres.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    private fun getSelectedGenre(itemPosition: Int): GenreType {
        val map = Constants.getMovieGenresMap()
        return map[Constants.getMovieGenresArray()[itemPosition]] ?: error(Constants.GENRE_TYPE_ERROR_MESSAGE)
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