package com.github.androidpirate.capsulereviews.ui.movie.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.movie.Genre
import com.github.androidpirate.capsulereviews.data.response.movie.MovieResponse
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.detail_action_bar.*
import kotlinx.android.synthetic.main.detail_similar.*
import kotlinx.android.synthetic.main.movie_header.*
import kotlinx.android.synthetic.main.movie_info.*
import kotlinx.android.synthetic.main.movie_summary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class MovieDetailFragment : Fragment(), ItemClickListener {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var movie: MovieResponse
    private lateinit var adapter: ListItemAdapter<MoviesListItem>
    private lateinit var similarMovies: List<MoviesListItem>

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
        val fragmentName = MovieDetailFragment::class.simpleName
        adapter = ListItemAdapter(MovieDetailFragment::class.simpleName,this)
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                movie = apiService.getMovieDetails(args.movieId)
            }
            withContext(Dispatchers.IO) {
                similarMovies = apiService.getSimilarMovies(args.movieId).moviesListItems
            }
            setupViews()
        }
    }

    private fun setupViews() {
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w185/" + movie.posterPath)
            .into(moviePoster)
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w185/" + movie.posterPath)
            .apply(bitmapTransform(BlurTransformation(100)))
            .into(movieHeaderBg)
        movieTitle.text = movie.title
        movieTagLine.text = movie.tagline
        // TODO 5: Content rating requires a ReleasesResponse
        releaseDate.text = formatReleaseDate(movie.releaseDate)
        genres.text = formatGenres(movie.genres)
        userRating.text = movie.voteAverage.toString()
        runTime.text = formatRunTime(movie.runtime)
        overview.text = movie.overview
        budget.text = formatBudget(movie.budget)
        revenue.text = formatRevenue(movie.revenue)
        setIMDBLink(movie.imdbId)
        adapter.submitList(similarMovies)
        rvSimilar.layoutManager = GridLayoutManager(requireContext(), 3)
        rvSimilar.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
        rvSimilar.adapter = adapter
    }

    private fun formatReleaseDate(releaseDate: String): String {
        val dateArray = releaseDate.split("-")
        val year = dateArray[0]
        val month = dateArray[1]
        val day = dateArray[2]
        return "$day/$month/$year"
    }

    private fun formatRunTime(runTime: Int): String {
        val hour = runTime / 60
        val minute = runTime % 60
        return "$hour h $minute m"
    }

    private fun formatGenres(genres: List<Genre>): String {
        var movieGenres: String = ""
        for(i in genres.indices) {
            if(i == genres.size - 1) {
                movieGenres += genres[i].name
                break
            }
            movieGenres += "${genres[i].name}, "
        }
        return movieGenres
    }

    private fun formatBudget(budget: Int): String {
        val formatter = DecimalFormat("#,###")
        return "$ ${formatter.format(budget)}"
    }

    private fun formatRevenue(revenue: Int): String {
        val formatter = DecimalFormat("#,###")
        return "$ ${formatter.format(revenue)}"
    }

    private fun setIMDBLink(endpoint: String) {
        val imdbBaseURL = "https://www.imdb.com/title/"
        val imdbEndpoint = "${imdbBaseURL + endpoint}/"
        imdbLink.setOnClickListener {
            val uri = Uri.parse(imdbEndpoint);
            val intent = Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    override fun <T> onItemClick(item: T) {
        TODO("Not yet implemented")
    }
}