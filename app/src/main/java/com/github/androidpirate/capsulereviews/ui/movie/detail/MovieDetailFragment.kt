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
    private lateinit var adapter: SimilarContentAdapter<NetworkMoviesListItem>
    private lateinit var similarMovies: List<NetworkMoviesListItem>
    private lateinit var viewModel: MovieDetailViewModel
    private var videoKey: String = Constants.EMPTY_VIDEO_KEY
    private var imdbEndpoint: String = Constants.EMPTY_FIELD_STRING
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
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
        viewModel.getMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
            networkMovie = it
            setMoviePoster()
            setMovieDetails()
            setFavoriteButtonState()
            setIMDBLink()
        })
        viewModel.getIsFavorite().observe(viewLifecycleOwner, Observer {
            if(it != null) {
                isFavorite = it
                setFavoriteButtonState()
            }
        })
        viewModel.getMovieKey(args.movieId).observe(viewLifecycleOwner, Observer {
            videoKey = it
            setTrailerLink()
        })
        viewModel.getSimilarMovies(args.movieId).observe(viewLifecycleOwner, Observer {
            similarMovies = it
            setSimilarMovies()
        })
        viewModel.getImdbEndpoint().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty() || it.isNotBlank()) {
                imdbEndpoint = it
            }
        })
        setupUpNavigation()
        setFavoriteListener()
        setShareListener()
        displayContainerScreen()
    }

    override fun onResume() {
        super.onResume()
        setFlagDecorationOn()
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
            .placeholder(R.drawable.ic_image_placeholder_white)
            .apply(bitmapTransform(BlurTransformation(100)))
            .into(movieHeaderBg)
        Glide.with(requireContext())
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.ADAPTER_POSTER_WIDTH +
                        networkMovie.posterPath)
            .placeholder(R.drawable.ic_image_placeholder_white)
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

    private fun setFavoriteButtonState() {
        btFavorite.isActivated = isFavorite
    }

    private fun setIMDBLink() {
        val imdbEndpoint = BuildConfig.IMDB_BASE_URL + networkMovie.imdbId
        viewModel.setImdbEndpoint(imdbEndpoint)
        this.imdbLink.setOnClickListener {
            if(imdbEndpoint != BuildConfig.IMDB_BASE_URL) {
                val uri = Uri.parse(imdbEndpoint)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.link_not_available_toast_content),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupUpNavigation() {
        btUp.setOnClickListener {
            findNavController().navigate(R.id.action_movie_detail_to_list)
        }
    }

    private fun setFavoriteListener() {
        btFavorite.setOnClickListener {
            if(!isFavorite) {
                viewModel.insertFavoriteMovie(networkMovie)
                setFavoriteButtonState()
            } else {
                viewModel.deleteFavoriteMovie(networkMovie)
                setFavoriteButtonState()
            }
        }
    }

    private fun setShareListener() {
        btShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, Constants.INTENT_MOVIE_SUBJECT)
                putExtra(Intent.EXTRA_TEXT, imdbEndpoint)
                type = Constants.INTENT_TYPE_TEXT
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
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
            rvSimilar.adapter = adapter
        }
    }

    private fun setFlagDecorationOn() {
        if(!viewModel.getFlagDecoration()) {
            rvSimilar.addItemDecoration(GridSpacingItemDecoration(4, 30, true))
        }
        viewModel.setFlagDecorationOn()
    }

    override fun <T> onItemClick(item: T) {
        viewModel.setFlagDecorationOff()
        val action = MovieDetailFragmentDirections
            .actionMovieDetailToSelf((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }
}