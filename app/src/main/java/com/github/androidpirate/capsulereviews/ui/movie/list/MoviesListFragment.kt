package com.github.androidpirate.capsulereviews.ui.movie.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.network.response.movie.NetworkMovie
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.list.ListItemAdapter
import com.github.androidpirate.capsulereviews.ui.adapter.list.ItemClickListener
import com.github.androidpirate.capsulereviews.ui.dialog.MovieGenresDialogFragment
import com.github.androidpirate.capsulereviews.util.internal.Constants
import com.github.androidpirate.capsulereviews.util.internal.FragmentType.*
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType
import com.github.androidpirate.capsulereviews.util.internal.GenericSortType.*
import com.github.androidpirate.capsulereviews.util.internal.GenreType
import com.github.androidpirate.capsulereviews.util.internal.NetworkType
import com.github.androidpirate.capsulereviews.viewmodel.MoviesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movies_list.*
import kotlinx.android.synthetic.main.movie_showcase.*
import kotlinx.android.synthetic.main.movie_toolbar.*
import kotlinx.android.synthetic.main.no_connection_screen.*

@AndroidEntryPoint
class MoviesListFragment :
    Fragment(),
    ItemClickListener,
    MovieGenresDialogFragment.MovieGenresDialogListener {

    private val viewModel: MoviesListViewModel by viewModels()
    private lateinit var popularNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var topRatedNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var nowPlayingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var upcomingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private lateinit var trendingNetworkMoviesAdapter: ListItemAdapter<NetworkMoviesListItem>
    private var showcaseVideoKey = Constants.EMPTY_VIDEO_KEY
    private var isDataOnline: Boolean = true
    private var isShowcaseFavorite = false

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLoadingScreen()
        viewModel.isOnline.observe(viewLifecycleOwner, Observer { isOnLine ->
            isDataOnline = isOnLine
            if(isOnLine) {
                setupViews()
                viewModel.popularMovies.observe(viewLifecycleOwner, Observer(popularNetworkMoviesAdapter::submitList))
                viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer(topRatedNetworkMoviesAdapter::submitList))
                viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer(nowPlayingNetworkMoviesAdapter::submitList))
                viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer(upcomingNetworkMoviesAdapter::submitList))
                viewModel.trendingMovies.observe(viewLifecycleOwner, Observer(trendingNetworkMoviesAdapter::submitList))
                viewModel.showcaseMovie.observe(viewLifecycleOwner, Observer { showcaseMovie ->
                    if(showcaseMovie != null) {
                        setShowcaseMovie(showcaseMovie)
                        viewModel.setIsShowcaseFavorite(showcaseMovie.id)
                    }
                })
                viewModel.showcaseVideoKey.observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        showcaseVideoKey = it
                    }
                })
                viewModel.getIsShowcaseFavorite().observe(viewLifecycleOwner, Observer {
                    if(it != null) {
                        this.isShowcaseFavorite = it
                        setFavoriteButtonState()
                    }
                })
                displayContainerScreen()
            } else {
                displayNoConnectionScreen()
            }
        })
    }

    private fun displayLoadingScreen() {
        container.visibility = View.GONE
        noConnectionScreen.visibility = View.GONE
        loadingScreen.visibility = View.VISIBLE
    }

    private fun displayNoConnectionScreen() {
        container.visibility = View.GONE
        loadingScreen.visibility = View.GONE
        noConnectionScreen.visibility = View.VISIBLE
        setRefreshListener()
    }

    private fun displayContainerScreen() {
        loadingScreen.visibility = View.GONE
        noConnectionScreen.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    private fun setupAdapters() {
        popularNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = POPULAR,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        topRatedNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = TOP_RATED,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        nowPlayingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = NOW_PLAYING,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        upcomingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = UPCOMING,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
        trendingNetworkMoviesAdapter = ListItemAdapter(
            fragment = MOVIE_LIST,
            clickListener = this,
            genericSort = TRENDING,
            network = NetworkType.ALL,
            genre = GenreType.ALL
        )
    }

    private fun setupViews() {
        rvPopular.adapter = popularNetworkMoviesAdapter
        rvTopRated.adapter = topRatedNetworkMoviesAdapter
        rvNowPlaying.adapter = nowPlayingNetworkMoviesAdapter
        rvUpcoming.adapter = upcomingNetworkMoviesAdapter
        rvTrending.adapter = trendingNetworkMoviesAdapter
        setGenreSpinnerListener()
    }

    private fun setGenreSpinnerListener() {
        movieGenreSpinner.setOnClickListener {
            showMovieGenresDialog()
        }
    }

    private fun setShowcaseMovie(showCaseMovie: NetworkMovie) {
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

    private fun setShowcaseMovieClickListeners(showCaseMovie: NetworkMovie) {
        setFavoriteListener(showCaseMovie)
        setPlayTrailerListener()
        setShowcaseDetailListener(showCaseMovie)
    }

    private fun setFavoriteListener(showCaseMovie: NetworkMovie) {
        scAddFavorite.setOnClickListener {
            if(!isShowcaseFavorite) {
                viewModel.insertShowcaseMovieToFavorites(showCaseMovie)
                setFavoriteButtonState()
            } else {
                viewModel.deleteShowcaseMovieFromFavorites(showCaseMovie)
                setFavoriteButtonState()
            }
        }
    }

    private fun setPlayTrailerListener() {
        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + showcaseVideoKey
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
    }

    private fun setShowcaseDetailListener(showCaseMovie: NetworkMovie) {
        scInfo.setOnClickListener {
            val action = MoviesListFragmentDirections.actionMoviesListToDetail(showCaseMovie.id)
            findNavController().navigate(action)
        }
    }

    private fun setRefreshListener() {
        refresh.setOnClickListener {
            val action = MoviesListFragmentDirections.actionMoviesListPopToSelf()
            findNavController().navigate(action)
        }
    }

    private fun setFavoriteButtonState() {
        scAddFavorite.isActivated = isShowcaseFavorite
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
                    .actionMoviesListToDetail((item as NetworkMoviesListItem).id, MOVIE_LIST)
                navigateToDetails(action)
            }
    }

    override fun onGenreSelected(genre: GenreType) {
        val action = MoviesListFragmentDirections
            .actionMoviesListToPagedMovies(POPULAR, genre)
        navigateToPagedMoviesList(action)
    }
}