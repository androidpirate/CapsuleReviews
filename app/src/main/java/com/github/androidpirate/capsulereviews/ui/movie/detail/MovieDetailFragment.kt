package com.github.androidpirate.capsulereviews.ui.movie.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.movie.MovieResponse
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.ui.movie.list.MovieListAdapter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.detail_similar.*
import kotlinx.android.synthetic.main.movie_header.*
import kotlinx.android.synthetic.main.movie_info.*
import kotlinx.android.synthetic.main.movie_summary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailFragment : Fragment(), ItemClickListener {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var movie: MovieResponse
    private lateinit var adapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_movie_detail_toList)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val apiService: MovieDbService = MovieDbService()
        adapter = MovieListAdapter(this)
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                movie = apiService.getMovieDetails(args.movieId)
            }
            withContext(Dispatchers.IO) {
                adapter.submitList(apiService.getSimilarMovies(args.movieId).moviesListItems)
            }
            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w185/" + movie.posterPath)
                .into(moviePoster)
            movieTitle.text = movie.title
            movieTagLine.text = movie.tagline
            // TODO 5: Content rating requires a ReleasesResponse
            releaseDate.text = movie.releaseDate
            var movieGenres: String ?= null
            for(movieGenre in movie.genres) {
                movieGenres = "${movieGenre.name}, "
            }
            genres.text = movieGenres
            runTime.text = movie.runtime.toString()
            overview.text = movie.overview
            budget.text = movie.budget.toString()
            revenue.text = movie.revenue.toString()
            rvSimilar.addItemDecoration(GridSpacingItemDecoration(3, 0, true))
            rvSimilar.adapter = adapter
        }
    }

    override fun <T> onItemClick(item: T) {
        TODO("Not yet implemented")
    }
}