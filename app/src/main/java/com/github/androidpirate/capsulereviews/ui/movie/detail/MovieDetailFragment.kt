package com.github.androidpirate.capsulereviews.ui.movie.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.util.ContentFormatter
import com.github.androidpirate.capsulereviews.util.GridSpacingItemDecoration
import com.github.androidpirate.capsulereviews.ui.adapter.similar.SimilarContentAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.similar.SimilarContentClickListener
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import com.github.androidpirate.capsulereviews.viewmodel.MovieDetailViewModel
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.detail_action_bar.*
import kotlinx.android.synthetic.main.detail_similar.*
import kotlinx.android.synthetic.main.fragment_movie_detail.container
import kotlinx.android.synthetic.main.fragment_movie_detail.loadingScreen
import kotlinx.android.synthetic.main.movie_header.*
import kotlinx.android.synthetic.main.movie_info.*
import kotlinx.android.synthetic.main.movie_summary.*

class MovieDetailFragment : Fragment(), SimilarContentClickListener {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var networkMovie: NetworkMovie
    private var videoKey: String = ""
    private lateinit var adapter: SimilarContentAdapter<NetworkMoviesListItem>
    private lateinit var similarMovies: List<NetworkMoviesListItem>
    private lateinit var viewModel: MovieDetailViewModel
    private var flagDecoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = SimilarContentAdapter(fragment = MOVIE_DETAIL, clickListener = this)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
        })
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
        displayLoadingScreen()
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_movie_detail_to_list)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
        viewModel.getMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
            networkMovie = it
            setMoviePoster()
            setMovieDetails()
            setIMDBLink()
        })
        viewModel.getMovieKey(args.movieId).observe(viewLifecycleOwner, Observer {
            videoKey = it
            setTrailerLink()
        })
        viewModel.getSimilarMovies(args.movieId).observe(viewLifecycleOwner, Observer {
            similarMovies = it
            setSimilarMovies()
        })
        displayContainerScreen()
    }

    private fun displayLoadingScreen() {
        loadingScreen.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setMoviePoster() {
        Glide.with(requireContext())
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.ADAPTER_POSTER_WIDTH +
                        networkMovie.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .apply(bitmapTransform(BlurTransformation(100)))
            .into(movieHeaderBg)
        Glide.with(requireContext())
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.ADAPTER_POSTER_WIDTH +
                        networkMovie.posterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(moviePoster)
    }

    private fun setMovieDetails() {
        movieTitle.text = networkMovie.title
        movieTagLine.text = networkMovie.tagLine
        releaseDate.text = ContentFormatter.formatReleaseDate(networkMovie.releaseDate)
        genres.text = ContentFormatter.formatGenres(networkMovie.networkGenres)
        userRating.text = networkMovie.voteAverage.toString()
        runTime.text = ContentFormatter.formatMovieRunTime(networkMovie.runtime)
        overview.text = networkMovie.overview
        budget.text = ContentFormatter.formatBudget(networkMovie.budget)
        revenue.text = ContentFormatter.formatRevenue(networkMovie.revenue)
    }

    private fun setIMDBLink() {
        val imdbEndpoint = BuildConfig.IMDB_BASE_URL + networkMovie.imdbId
        imdbLink.setOnClickListener {
            if(imdbEndpoint != BuildConfig.IMDB_BASE_URL) {
                val uri = Uri.parse(imdbEndpoint);
                val intent = Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.link_not_available_toast_content),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setTrailerLink() {
        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + videoKey
        btPlay.setOnClickListener {
            if(videoKey != Constants.EMPTY_VIDEO_KEY) {
                val uri = Uri.parse(trailerEndpoint)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.video_no_available_toast_content),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setSimilarMovies() {
        if(similarMovies.isEmpty()) {
            emptyListMessage.visibility = View.VISIBLE
            rvSimilar.visibility = View.INVISIBLE
        } else {
            adapter.submitList(similarMovies)
            rvSimilar.layoutManager = GridLayoutManager(requireContext(), 3)
            if(!flagDecoration) {
                rvSimilar.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
                setFlagDecorationOn()
            }
            rvSimilar.adapter = adapter
        }
    }

    private fun setFlagDecorationOn() {
        flagDecoration = true
    }

    override fun <T> onItemClick(item: T) {
        val action = MovieDetailFragmentDirections
            .actionMovieDetailFragmentSelf((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }
}