package com.github.androidpirate.capsulereviews.ui.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.androidpirate.capsulereviews.R
import com.github.androidpirate.capsulereviews.data.api.MovieDbService
import com.github.androidpirate.capsulereviews.util.ItemClickListener
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListFragment : Fragment(), ItemClickListener {
    private lateinit var popularMoviesAdapter: MovieListAdapter
    private lateinit var topRatedMoviesAdapter: MovieListAdapter
    private lateinit var nowPlayingMoviesAdapter: MovieListAdapter
    private lateinit var upcomingMoviesAdapter: MovieListAdapter
    private lateinit var trendingMoviesAdapter: MovieListAdapter

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
        popularMoviesAdapter = MovieListAdapter(this)
        topRatedMoviesAdapter = MovieListAdapter(this)
        nowPlayingMoviesAdapter = MovieListAdapter(this)
        upcomingMoviesAdapter = MovieListAdapter(this)
        trendingMoviesAdapter = MovieListAdapter(this)
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
                withContext(Dispatchers.IO) { apiService.getUpcomingMovies().moviesListItems
                }
            )
            trendingMoviesAdapter.submitList(
                withContext(Dispatchers.IO) {apiService.getTrendingMovies().moviesListItems
                }
            )
        }
        rvPopular.adapter = popularMoviesAdapter
        rvTopRated.adapter = topRatedMoviesAdapter
        rvNowPlaying.adapter = nowPlayingMoviesAdapter
        rvUpcoming.adapter = upcomingMoviesAdapter
        rvTrending.adapter = trendingMoviesAdapter
    }

    override fun <T> onItemClick(item: T) {
        findNavController().navigate(R.id.action_movie_list_toDetail)
    }
}