package com.github.androidpirate.capsulereviews.util

import com.github.androidpirate.capsulereviews.data.Movie
import com.github.androidpirate.capsulereviews.data.TvShow

class FakeData {

    companion object {
        @JvmStatic
        fun getMovies() : ArrayList<Movie> {
            return arrayListOf(
                Movie(), Movie(), Movie(), Movie(), Movie(),
                Movie(), Movie(), Movie(), Movie(), Movie()
            )
        }

        @JvmStatic
        fun getTvShows() : ArrayList<TvShow> {
            return arrayListOf(
                TvShow(), TvShow(), TvShow(), TvShow(), TvShow(),
                TvShow(), TvShow(), TvShow(), TvShow(), TvShow()
            )
        }
    }
}