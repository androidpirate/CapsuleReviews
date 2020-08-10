package com.github.androidpirate.capsulereviews.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.movies.MoviesListItem
import com.github.androidpirate.capsulereviews.ui.adapter.ListItemAdapter
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListFragment : Fragment(), ItemClickListener {
    private lateinit var popularMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var popularMovies: List<MoviesListItem>
    private lateinit var topRatedMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var topRatedMovies: List<MoviesListItem>
    private lateinit var nowPlayingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var nowPlayingMovies: List<MoviesListItem>
    private lateinit var upcomingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var upcomingMovies: List<MoviesListItem>
    private lateinit var trendingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var trendingMovies: List<MoviesListItem>

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

        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main ) {
            withContext(Dispatchers.IO) {
                popularMovies = apiService.getPopularMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                topRatedMovies = apiService.getTopRatedMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                nowPlayingMovies = apiService.getNowPlayingMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                upcomingMovies = apiService.getUpcomingMovies().moviesListItems
            }
            withContext(Dispatchers.IO) {
                trendingMovies = apiService.getTrendingMovies().moviesListItems
            }
            setupViews()
        }
    }

    private fun setupAdapters() {
        popularMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        topRatedMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        nowPlayingMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        upcomingMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
        trendingMoviesAdapter = ListItemAdapter(
            MovieListFragment::class.simpleName, this)
    }

    private fun setupViews() {
        popularMoviesAdapter.submitList(popularMovies)
        rvPopular.adapter = popularMoviesAdapter
        topRatedMoviesAdapter.submitList(topRatedMovies)
        rvTopRated.adapter = topRatedMoviesAdapter
        nowPlayingMoviesAdapter.submitList(nowPlayingMovies)
        rvNowPlaying.adapter = nowPlayingMoviesAdapter
        upcomingMoviesAdapter.submitList(upcomingMovies)
        rvUpcoming.adapter = upcomingMoviesAdapter
        trendingMoviesAdapter.submitList(trendingMovies)
        rvTrending.adapter = trendingMoviesAdapter
    }

    override fun <T> onItemClick(item: T) {
        val action = MovieListFragmentDirections
            .actionMovieListToDetail((item as MoviesListItem).id)
        findNavController().navigate(action)
    }
}