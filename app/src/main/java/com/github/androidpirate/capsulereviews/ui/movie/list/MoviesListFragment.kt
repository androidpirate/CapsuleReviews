package com.github.androidpirate.capsulereviews.ui.movie.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.ui.adapter.list.ListItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.list.ItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.MovieGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.GenreType.*
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType.*
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import com.github.androidpirate.capsulereviews.viewmodel.MoviesListViewModel
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.movie_showcase.*
import kotlinx.android.synthetic.main.movie_toolbar.*

class MoviesListFragment :
    Fragment(),
    ItemClickListener,
    MovieGenresDialogFragment.MovieGenresDialogListener {

    private lateinit var popularNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var topRatedNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var nowPlayingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var upcomingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var trendingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var viewModel: MoviesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(MoviesListViewModel::class.java)
        displayLoadingScreen()
    }

    override fun onStart() {
        super.onStart()
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            popularNetworkMoviesAdapter.submitList(it)
        })
        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
            topRatedNetworkMoviesAdapter.submitList(it)
        })
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer {
            nowPlayingNetworkMoviesAdapter.submitList(it)
        })
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
            upcomingNetworkMoviesAdapter.submitList(it)
        })
        viewModel.trendingMovies.observe(viewLifecycleOwner, Observer {
            trendingNetworkMoviesAdapter.submitList(it)
        })
        setupViews()
        viewModel.showcaseMovie.observe(viewLifecycleOwner, Observer { it ->
            if(it != null) {
                setShowcaseMovie(it)
            }
        })
    }

    private fun displayLoadingScreen() {
        loadingScreen.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setupAdapters() {
        popularNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = POPULAR,
            network = NONE,
            genre = ALL
        )
        topRatedNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = TOP_RATED,
            network = NONE,
            genre = ALL
        )
        nowPlayingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = NOW_PLAYING,
            network = NONE,
            genre = ALL
        )
        upcomingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = UPCOMING,
            network = NONE,
            genre = ALL
        )
        trendingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = TRENDING,
            network = NONE,
            genre = ALL
        )
    }

    private fun setupViews() {
        rvPopular.adapter = popularNetworkMoviesAdapter
        rvTopRated.adapter = topRatedNetworkMoviesAdapter
        rvNowPlaying.adapter = nowPlayingNetworkMoviesAdapter
        rvUpcoming.adapter = upcomingNetworkMoviesAdapter
        rvTrending.adapter = trendingNetworkMoviesAdapter
        movieGenreSpinner.setOnClickListener {
            showMovieGenresDialog()
        }
    }

    private fun setShowcaseMovie(showCaseMovie: DBMovieShowcase) {
        setShowCaseMoviePoster(showCaseMovie.posterPath)
        setShowcaseMovieTitle(showCaseMovie.title)
        setShowcaseMovieClickListeners(showCaseMovie)
    }

    private fun setShowCaseMoviePoster(showCaseMoviePosterPath: String) {
        Glide.with(requireContext())
            .load(
                BuildConfig.MOVIE_DB_IMAGE_BASE_URL +
                        Constants.SHOWCASE_POSTER_WIDTH +
                        showCaseMoviePosterPath)
            .placeholder(R.drawable.ic_image_placeholder_white)
            .into(scPoster)
    }

    private fun setShowcaseMovieTitle(showcaseMovieTitle: String) {
        scTitle.text = showcaseMovieTitle
    }

    private fun setShowcaseMovieClickListeners(showCaseMovie: DBMovieShowcase) {
        scAddFavorite.setOnClickListener {
            // TODO 7: Implement adding to favorites here
        }

        scInfo.setOnClickListener {
            onShowcaseMovieClick(showCaseMovie)
        }

        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + showCaseMovie.videoKey
        scPlay.setOnClickListener {
            if(trailerEndpoint != BuildConfig.YOUTUBE_BASE_URL) {
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
        displayContainerScreen()
    }

    private fun onShowcaseMovieClick(showCaseMovie: DBMovieShowcase) {
        val action = MoviesListFragmentDirections.actionMoviesListToDetail(showCaseMovie.movieId)
        findNavController().navigate(action)
    }

    private fun showMovieGenresDialog() {
        val genresDialog = MovieGenresDialogFragment
            .newInstance(this, Constants.DEFAULT_SELECTED_POSITION)
        genresDialog.show(requireActivity().supportFragmentManager, Constants.MOVIES_LIST_FRAG_TAG)
    }

    private fun navigateToPagedMoviesList(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToDetails(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun <T> onItemClick(
        item: T,
        isLast: Boolean,
        genericSort: GenericSortType,
        network: NetworkType,
        genre: GenreType) {
            if(isLast) {
                when(genericSort) {
                    POPULAR -> {
                        val action = MoviesListFragmentDirections
                            .actionMoviesListToPagedMovies(POPULAR)
                        navigateToPagedMoviesList(action)
                    }
                    TOP_RATED -> {
                        val action = MoviesListFragmentDirections
                            .actionMoviesListToPagedMovies(TOP_RATED)
                        navigateToPagedMoviesList(action)
                    }
                    NOW_PLAYING -> {
                        val action = MoviesListFragmentDirections
                            .actionMoviesListToPagedMovies(NOW_PLAYING)
                        navigateToPagedMoviesList(action)
                    }
                    UPCOMING -> {
                        val action = MoviesListFragmentDirections
                            .actionMoviesListToPagedMovies(UPCOMING)
                        navigateToPagedMoviesList(action)
                    }
                    TRENDING -> {
                        val action = MoviesListFragmentDirections
                            .actionMoviesListToPagedMovies(TRENDING)
                        navigateToPagedMoviesList(action)
                    }
                }
            } else {
                val action = MoviesListFragmentDirections
                    .actionMoviesListToDetail((item as DBMovie).id)
                navigateToDetails(action)
            }
    }

    override fun onGenreSelected(genre: GenreType) {
        val action = MoviesListFragmentDirections
            .actionMoviesListToPagedMovies(POPULAR, genre)
        navigateToPagedMoviesList(action)
    }
}