package com.github.androidpirate.capsulereviews.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.GenreAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.dialog.GenreClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import kotlinx.android.synthetic.main.tv_show_genres_dialog.*

class TvShowGenresDialogFragment(
    private val listener: TvShowGenresDialogListener,
    private val selectedPosition: Int):
    DialogFragment(),
    GenreClickListener {

    private lateinit var adapter: GenreAdapter

    interface TvShowGenresDialogListener {
        fun onGenreSelected(genre: GenreType)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GenreAdapter(Constants.getTvGenresArray(), this, selectedPosition)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tv_show_genres_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
        tvShowGenres.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun getSelectedGenre(itemPosition: Int): GenreType {
        val map = Constants.getTvGenresMap()
        return map[Constants.getTvGenresArray()[itemPosition]] ?: error(Constants.GENRE_TYPE_ERROR_MESSAGE)
    }

    override fun onGenreClick(itemPosition: Int) {
        listener.onGenreSelected(getSelectedGenre(itemPosition))
        dialog?.cancel()
    }

    companion object {
        fun newInstance(listener: TvShowGenresDialogListener, selectedPosition: Int): TvShowGenresDialogFragment {
            return TvShowGenresDialogFragment(listener, selectedPosition)
        }
    }
}