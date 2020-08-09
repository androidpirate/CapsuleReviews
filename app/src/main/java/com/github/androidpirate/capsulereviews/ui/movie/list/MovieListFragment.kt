package com.github.androidpirate.capsulereviews.ui.movie.list

import android.graphics.Movie
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.data.response.movie.MovieResponse
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
    private lateinit var topRatedMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var nowPlayingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var upcomingMoviesAdapter: ListItemAdapter<MoviesListItem>
    private lateinit var trendingMoviesAdapter: ListItemAdapter<MoviesListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        popularMoviesAdapter = ListItemAdapter(MovieListFragment::class.simpleName, this)
        topRatedMoviesAdapter = ListItemAdapter(MovieListFragment::class.simpleName, this)
        nowPlayingMoviesAdapter = ListItemAdapter(MovieListFragment::class.simpleName, this)
        upcomingMoviesAdapter = ListItemAdapter(MovieListFragment::class.simpleName, this)
        trendingMoviesAdapter = ListItemAdapter(MovieListFragment::class.simpleName, this)
        val apiService = MovieDbService()
        GlobalScope.launch(Dispatchers.Main ) {

            popularMoviesAdapter.submitList(
                withContext(Dispatchers.IO) { apiService.getPopularMovies().moviesListItems }
            )
            topRatedMoviesAdapter.submitList(
                withContext(Dispatchers.IO) { apiService.getTopRatedMovies().moviesListItems }
            )
            nowPlayingMoviesAdapter.submitList(
                withContext(Dispatchers.IO) { apiService.getNowPlayingMovies().moviesListItems }
            )
            upcomingMoviesAdapter.submitList(
                withContext(Dispatchers.IO) { apiService.getUpcomingMovies().moviesListItems }
            )
            trendingMoviesAdapter.submitList(
                withContext(Dispatchers.IO) {apiService.getTrendingMovies().moviesListItems }
            )
        }
        rvPopular.adapter = popularMoviesAdapter
        rvTopRated.adapter = topRatedMoviesAdapter
        rvNowPlaying.adapter = nowPlayingMoviesAdapter
        rvUpcoming.adapter = upcomingMoviesAdapter
        rvTrending.adapter = trendingMoviesAdapter
    }

    override fun <T> onItemClick(item: T) {
        val action = MovieListFragmentDirections.actionMovieListToDetail((item as MoviesListItem).id)
        findNavController().navigate(action)
    }
}