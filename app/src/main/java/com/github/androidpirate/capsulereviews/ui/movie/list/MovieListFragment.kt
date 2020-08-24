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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.androidpirate.capsulereviews.BuildConfig
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovie
import com.github.androidpirate.capsulereviews.data.db.entity.DBMovieShowcase
import com.github.androidpirate.capsulereviews.data.network.response.movies.NetworkMoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import com.github.androidpirate.capsulereviews.viewmodel.ViewModelFactory
import com.github.androidpirate.capsulereviews.viewmodel.MovieListViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.movie_showcase.*

class MovieListFragment : Fragment(), ItemClickListener {
    private lateinit var popularNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var topRatedNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var nowPlayingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var upcomingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var trendingNetworkMoviesAdapter: ListItemAdapter<DBMovie>
    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(MovieListViewModel::class.java)
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
            setShowcaseMovie(it)
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
            MovieListFragment::class.simpleName, this)
        topRatedNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        nowPlayingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        upcomingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        trendingNetworkMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
    }

    private fun setupViews() {
        rvPopular.adapter = popularNetworkMoviesAdapter
        rvTopRated.adapter = topRatedNetworkMoviesAdapter
        rvNowPlaying.adapter = nowPlayingNetworkMoviesAdapter
        rvUpcoming.adapter = upcomingNetworkMoviesAdapter
        rvTrending.adapter = trendingNetworkMoviesAdapter
    }

    private fun setShowcaseMovie(showCaseMovie: DBMovieShowcase?) {
        setShowCaseMoviePoster(showCaseMovie?.posterPath)
        setShowcaseMovieTitle(showCaseMovie?.title)
        setShowcaseMovieClickListeners(showCaseMovie)
    }

    private fun setShowCaseMoviePoster(showCaseMoviePosterPath: String?) {
        Glide.with(requireContext())
            .load(BuildConfig.MOVIE_DB_IMAGE_BASE_URL + "w342/" + showCaseMoviePosterPath)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(scPoster)
    }

    private fun setShowcaseMovieTitle(showcaseMovieTitle: String?) {
        scTitle.text = showcaseMovieTitle
    }

    private fun setShowcaseMovieClickListeners(showCaseMovie: DBMovieShowcase?) {
        scAddFavorite.setOnClickListener {
            // TODO 7: Implement adding to favorites here
        }

        scInfo.setOnClickListener {
            onItemClick(showCaseMovie)
        }

        val trailerEndpoint = BuildConfig.YOUTUBE_BASE_URL + showCaseMovie?.videoKey
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

    override fun <T> onItemClick(item: T) {
        val action = MovieListFragmentDirections
            .actionMovieListToDetail((item as NetworkMoviesListItem).id)
        findNavController().navigate(action)
    }
}